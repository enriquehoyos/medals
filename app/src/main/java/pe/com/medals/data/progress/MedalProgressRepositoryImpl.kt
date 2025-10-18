package pe.com.medals.data.progress

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pe.com.medals.data.datastore.DataStoreManager
import pe.com.medals.domain.model.MedalProgress
import pe.com.medals.domain.repository.MedalProgressRepository

/**
 * Created by Quique on 10/16/2025.
 */

class MedalProgressRepositoryImpl(
    private val ds: DataStoreManager
): MedalProgressRepository {

    private val KEY = stringPreferencesKey("medals_progress_json")
    private val json = Json { ignoreUnknownKeys = true }

    override val progressFlow: Flow<List<MedalProgress>> =
        ds.data.map { prefs ->
            val txt = prefs[KEY].orEmpty()
            if (txt.isBlank()) emptyList()
            else json.decodeFromString<List<MedalProgressDTO>>(txt)
                .map { MedalProgress(it.id, it.level, it.points, it.isLocked) }
        }
    override suspend fun upsert(list: List<MedalProgress>) {
        ds.edit { prefs ->
            val dto = list.map { MedalProgressDTO(it.id, it.level, it.points, it.isLocked) }
            prefs[KEY] = json.encodeToString(dto)
        }
    }

    override suspend fun resetAll(ids: List<String>, defaultLevel: Int) {
        upsert(ids.map { MedalProgress(it, level = defaultLevel, points = 0, isLocked = false) })
    }

}

