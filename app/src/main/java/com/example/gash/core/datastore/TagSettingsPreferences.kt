package com.example.gash.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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
    private object Keys {
        val TAG1_ENABLED = booleanPreferencesKey("tag1_enabled")
        val TAG2_ENABLED = booleanPreferencesKey("tag2_enabled")
        val TAG3_ENABLED = booleanPreferencesKey("tag3_enabled")
    }

    val isTag1Enabled: Flow<Boolean> = context.tagSettingsDataStore.data.map { it[Keys.TAG1_ENABLED] ?: false }
    val isTag2Enabled: Flow<Boolean> = context.tagSettingsDataStore.data.map { it[Keys.TAG2_ENABLED] ?: false }
    val isTag3Enabled: Flow<Boolean> = context.tagSettingsDataStore.data.map { it[Keys.TAG3_ENABLED] ?: false }

    suspend fun setTag1Enabled(enabled: Boolean) = context.tagSettingsDataStore.edit { it[Keys.TAG1_ENABLED] = enabled }
    suspend fun setTag2Enabled(enabled: Boolean) = context.tagSettingsDataStore.edit { it[Keys.TAG2_ENABLED] = enabled }
    suspend fun setTag3Enabled(enabled: Boolean) = context.tagSettingsDataStore.edit { it[Keys.TAG3_ENABLED] = enabled }
}