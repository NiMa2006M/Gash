package com.example.gash.data.repository

import com.example.gash.core.datastore.ActivationPreferences
import com.example.gash.domain.repository.ActivationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ActivationRepositoryImpl @Inject constructor(
    private val prefs: ActivationPreferences
) : ActivationRepository {
    override val isActivated: Flow<Boolean> = prefs.isActivated
    override val farmName: Flow<String?> = prefs.farmName

    override suspend fun saveActivation(farmName: String, farmId: String, phoneNumber: String) =
        prefs.saveActivation(farmName, farmId, phoneNumber)

    override suspend fun clearActivation() = prefs.clearActivation()
}