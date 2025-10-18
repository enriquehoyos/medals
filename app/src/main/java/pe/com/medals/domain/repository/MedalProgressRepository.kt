package pe.com.medals.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.com.medals.domain.model.MedalProgress

/**
 * Created by Quique on 10/16/2025.
 */

interface MedalProgressRepository {
    val progressFlow: Flow<List<MedalProgress>>
    suspend fun upsert(list: List<MedalProgress>)
    suspend fun resetAll(ids: List<String>, defaultLevel: Int = 1)
}