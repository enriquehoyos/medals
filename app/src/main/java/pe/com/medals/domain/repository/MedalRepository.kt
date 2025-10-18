package pe.com.medals.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.com.medals.domain.model.Medal

/**
 * Created by Quique on 10/16/2025.
 */
interface MedalRepository {
    suspend fun refreshIfNeeded()
    fun getModels(): Flow<List<Medal>>
}