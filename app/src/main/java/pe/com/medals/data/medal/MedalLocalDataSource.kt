package pe.com.medals.data.medal

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import pe.com.medals.domain.model.Medal

/**
 * Created by Quique on 10/16/2025.
 */

class MedalLocalDataSource(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun loadModel(): List<Medal> = withContext(Dispatchers.IO) {
        val txt = context.assets.open("medals.json").bufferedReader().use { it.readText() }
        json.decodeFromString<List<MedalDTO>>(txt).map { it.toDomain() }
    }
}