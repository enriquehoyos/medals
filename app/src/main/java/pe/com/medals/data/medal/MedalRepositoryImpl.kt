package pe.com.medals.data.medal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pe.com.medals.domain.model.Medal
import pe.com.medals.domain.repository.MedalRepository

/**
 * Created by Quique on 10/16/2025.
 */

class MedalRepositoryImpl(
    private val local: MedalLocalDataSource
): MedalRepository {
    private val  state = MutableStateFlow<List<Medal>>(emptyList())

    override fun getModels(): Flow<List<Medal>> = state

    override suspend fun refreshIfNeeded() {
        if(state.value.isEmpty()){
            state.value = local.loadModel()
        }
    }
}