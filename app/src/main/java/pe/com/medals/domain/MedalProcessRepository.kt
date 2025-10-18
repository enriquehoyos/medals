package pe.com.medals.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import pe.com.medals.domain.model.Medal
import pe.com.medals.domain.model.MedalProgress
import pe.com.medals.domain.repository.MedalProgressRepository
import pe.com.medals.domain.repository.MedalRepository

/**
 * Created by Quique on 10/16/2025.
 */

class MedalProcessRepository(
    private val defsRepo: MedalRepository,
    private val progRepo: MedalProgressRepository
) {
    val uiFlow: Flow<List<Pair<Medal, MedalProgress>>> =
        combine(defsRepo.getModels(), progRepo.progressFlow) { defs, progs ->
            val progressById = progs.associateBy { it.id }
            defs.map { d ->
                val p = progressById[d.id] ?: MedalProgress(d.id)
                d to p
            }
        }

    suspend fun ensureInitialized() {
        defsRepo.refreshIfNeeded()
        val defs = defsRepo.getModels().first()
        val have = progRepo.progressFlow.first().associateBy { it.id }
        val merged = defs.map { d -> have[d.id] ?: MedalProgress(d.id) }
        progRepo.upsert(merged)
    }

    suspend fun saveProgress(list: List<MedalProgress>) = progRepo.upsert(list)

    suspend fun resetAll() {
        val ids = defsRepo.getModels().first().map { it.id }
        progRepo.resetAll(ids)
    }


}