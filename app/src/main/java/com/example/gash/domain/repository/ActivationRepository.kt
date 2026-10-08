package com.example.gash.domain.repository

import kotlinx.coroutines.flow.Flow

interface ActivationRepository {
    val isActivated: Flow<Boolean>
    val farmName: Flow<String?>
    val farmId: Flow<String?>
    val phoneNumber: Flow<String?>
    suspend fun saveActivation(farmName: String, farmId: String, phoneNumber: String)
    suspend fun clearActivation()
}