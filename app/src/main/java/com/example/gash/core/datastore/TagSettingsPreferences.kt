package com.example.gash.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.tagSettingsDataStore by preferencesDataStore(name = "tag_settings_prefs")

@Singleton
class TagSettingsPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun observeTags(slots: IntRange): Flow<List<StoredTag>> =
        context.tagSettingsDataStore.data.map { prefs ->
            slots.map { slot ->
                StoredTag(
                    slot = slot,
                    isEnabled = prefs[enabledKey(slot)] ?: false,
                    name = prefs[nameKey(slot)].orEmpty()
                )
            }
        }

    suspend fun setEnabled(slot: Int, enabled: Boolean) {
        context.tagSettingsDataStore.edit { it[enabledKey(slot)] = enabled }
    }

    suspend fun setName(slot: Int, name: String) {
        context.tagSettingsDataStore.edit { it[nameKey(slot)] = name }
    }

    private fun enabledKey(slot: Int) = booleanPreferencesKey("tag${slot}_enabled")
    private fun nameKey(slot: Int) = stringPreferencesKey("tag${slot}_name")
}