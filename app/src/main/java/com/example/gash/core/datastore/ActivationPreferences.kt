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

private val Context.dataStore by preferencesDataStore(name = "activation_prefs")


@Singleton
class ActivationPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val IS_ACTIVATED = booleanPreferencesKey("is_activated")
        val FARM_NAME = stringPreferencesKey("farm_name")
        val FARM_ID = stringPreferencesKey("farm_id")
        val PHONE_NUMBER = stringPreferencesKey("phone_number")
    }

    val isActivated: Flow<Boolean> = context.dataStore.data.map { it[Keys.IS_ACTIVATED] ?: false }
    val farmName: Flow<String?> = context.dataStore.data.map { it[Keys.FARM_NAME] }
    val farmId: Flow<String?> = context.dataStore.data.map { it[Keys.FARM_ID] }
    val phoneNumber: Flow<String?> = context.dataStore.data.map { it[Keys.PHONE_NUMBER] }

    suspend fun saveActivation(farmName: String, farmId: String, phoneNumber: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.IS_ACTIVATED] = true
            prefs[Keys.FARM_NAME] = farmName
            prefs[Keys.FARM_ID] = farmId
            prefs[Keys.PHONE_NUMBER] = phoneNumber
        }
    }

    suspend fun clearActivation() {
        context.dataStore.edit { it.clear() }
    }
}