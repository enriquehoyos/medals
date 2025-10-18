package pe.com.medals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pe.com.medals.domain.MedalProcessRepository
import pe.com.medals.domain.model.Medal
import pe.com.medals.domain.model.MedalProgress
import pe.com.medals.domain.model.UiMedal
import javax.inject.Inject
import java.util.Random

/**
 * Created by Quique on 10/16/2025.
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val medalRepo: MedalProcessRepository
) : ViewModel() {

    data class UiMedal(val def: Medal, val prog: MedalProgress)

    val medals: StateFlow<List<UiMedal>> =
        medalRepo.uiFlow.map { list -> list.map { (d,p) -> UiMedal(d,p) } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _pointsGained = MutableSharedFlow<Int>(extraBufferCapacity = 32)
    val pointsGained: SharedFlow<Int> = _pointsGained

    private val _levelUp = MutableSharedFlow<UiMedal>(extraBufferCapacity = 16)
    val levelUp: SharedFlow<UiMedal> = _levelUp

    private var engineJob: Job? = null
    private val isRunning = MutableStateFlow(false)
    private val _resetEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val resetEvent: SharedFlow<Unit> = _resetEvent

    init {
        viewModelScope.launch { medalRepo.ensureInitialized() }
    }

    fun resumeEngine() {
        if (isRunning.value) return
        isRunning.value = true
        engineJob = viewModelScope.launch(Dispatchers.Default) { engineLoop() }
    }

    fun pauseEngine() { isRunning.value = false; engineJob?.cancel(); engineJob = null }

    private suspend fun engineLoop() {
        val rnd = java.util.Random()
        while (isRunning.value) {
            //3 segundos para que pueda msotrarse la cantidad de puntos ganados en un Toast
            delay((3000L..3600L).random())

            val list = medals.value
            if (list.isEmpty()) continue

            val maxLevel = list.size
            val first = list.first()
            val currentLevel = first.prog.level.coerceIn(1, maxLevel)
            val currentPoints = first.prog.points

            if (currentLevel >= maxLevel && currentPoints == 0) continue

            val inc = (1..100).random()
            _pointsGained.tryEmit(inc)

            var lvl = currentLevel
            var pts = currentPoints + inc
            var didLevelUp = false

            if (pts >= 100) {
                pts -= 100
                lvl += 1
                if (lvl > maxLevel) {
                    lvl = maxLevel
                    pts = 0
                } else {
                    didLevelUp = true
                }
            }

            val updatedProgress = list.mapIndexed { index, it ->
                if (index != 0) it.prog
                else it.prog.copy(level = lvl, points = pts)
            }
            medalRepo.saveProgress(updatedProgress)

            if (didLevelUp) {
                val newStageIndex = (lvl - 1).coerceIn(0, maxLevel - 1)
                val newDef = list[newStageIndex].def
                _levelUp.tryEmit(UiMedal(newDef, first.prog.copy(level = lvl, points = pts)))
            }
        }
    }

    suspend fun resetAll() {
        pauseEngine()
        medalRepo.resetAll()
        delay(120)
        resumeEngine()
        _resetEvent.tryEmit(Unit)
    }
}
