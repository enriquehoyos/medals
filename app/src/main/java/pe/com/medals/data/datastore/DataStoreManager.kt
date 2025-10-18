package pe.com.medals.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow

/**
 * Created by Quique on 10/16/2025.
 */

val Context.appDataStore by preferencesDataStore("app_preferences")

class DataStoreManager(private val context: Context) {
    val data: Flow<androidx.datastore.preferences.core.Preferences> get() = context.appDataStore.data
    suspend fun <T> edit(block: suspend (androidx.datastore.preferences.core.MutablePreferences) -> T):
            androidx.datastore.preferences.core.Preferences =
        context.appDataStore.edit { block(it) }
}