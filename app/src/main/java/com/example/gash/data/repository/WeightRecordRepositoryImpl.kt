package com.example.gash.data.repository

import com.example.gash.core.database.dao.WeightRecordDao
import com.example.gash.core.database.entity.WeightRecordEntity
import com.example.gash.data.mapper.toDomain
import com.example.gash.domain.model.WeightRecord
import com.example.gash.domain.repository.WeightRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeightRecordRepositoryImpl @Inject constructor(
    private val dao: WeightRecordDao
) : WeightRecordRepository {

    override fun observeHistory(animalId: Long): Flow<List<WeightRecord>> =
        dao.observeHistory(animalId).map { list -> list.map { it.toDomain() } }

    override suspend fun getLatest(animalId: Long): WeightRecord? =
        dao.getLatest(animalId)?.toDomain()

    override suspend fun recordWeight(animalId: Long, weightKg: Double, recordedAt: Long) {
        dao.insert(WeightRecordEntity(animalId = animalId, weightKg = weightKg, recordedAt = recordedAt))
    }
}