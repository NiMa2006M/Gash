package com.example.gash.domain.repository

import com.example.gash.domain.model.WeightRecord
import kotlinx.coroutines.flow.Flow

interface WeightRecordRepository {
    fun observeHistory(animalId: Long): Flow<List<WeightRecord>>
    suspend fun getLatest(animalId: Long): WeightRecord?
    suspend fun recordWeight(animalId: Long, weightKg: Double, recordedAt: Long = System.currentTimeMillis())
}