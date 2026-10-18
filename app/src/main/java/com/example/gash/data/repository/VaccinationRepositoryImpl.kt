package com.example.gash.data.repository

import com.example.gash.core.database.dao.VaccinationDao
import com.example.gash.core.database.entity.VaccinationOperationEntity
import com.example.gash.core.database.entity.VaccinationRecordEntity
import com.example.gash.data.mapper.toDomain
import com.example.gash.domain.model.VaccinationOperation
import com.example.gash.domain.model.VaccinationRecord
import com.example.gash.domain.model.VaccinationSettings
import com.example.gash.domain.repository.VaccinationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VaccinationRepositoryImpl @Inject constructor(
    private val dao: VaccinationDao
) : VaccinationRepository {

    override suspend fun getOpenOperation(): VaccinationOperation? =
        dao.getOpenOperation()?.toDomain()

    override suspend fun getOperation(operationId: Long): VaccinationOperation? =
        dao.getOperation(operationId)?.toDomain()

    override suspend fun createOperation(settings: VaccinationSettings): Long =
        dao.insertOperation(
            VaccinationOperationEntity(
                vaccineTypeId = settings.vaccineTypeId,
                injectionSite = settings.injectionSite.name,
                doseMl = settings.doseMl,
                operatorName = settings.operatorName,
                herdId = settings.herdId,
                herdNameSnapshot = settings.herdName,
                startedAt = System.currentTimeMillis()
            )
        )

    override suspend fun finishOperation(operationId: Long) =
        dao.finishOperation(operationId, System.currentTimeMillis())

    override suspend fun deleteOperation(operationId: Long) = dao.deleteOperation(operationId)

    override fun observeRecords(operationId: Long): Flow<List<VaccinationRecord>> =
        dao.observeRecords(operationId).map { list -> list.map { it.toDomain() } }

    override suspend fun getRecord(recordId: Long): VaccinationRecord? =
        dao.getRecord(recordId)?.toDomain()

    override suspend fun hasRecord(operationId: Long, animalId: Long): Boolean =
        dao.countRecordsForAnimal(operationId, animalId) > 0

    override suspend fun addRecord(operationId: Long, animalId: Long, scannedRfidCode: String?): Long =
        dao.insertRecord(
            VaccinationRecordEntity(
                operationId = operationId,
                animalId = animalId,
                scannedRfidCode = scannedRfidCode,
                recordedAt = System.currentTimeMillis()
            )
        )

    override suspend fun deleteRecord(recordId: Long) = dao.deleteRecord(recordId)

    override suspend fun countRecords(operationId: Long): Int = dao.countRecords(operationId)

    override fun observeRecentOperatorNames(): Flow<List<String>> =
        dao.observeRecentOperatorNames(RECENT_OPERATOR_LIMIT)

    private companion object {
        const val RECENT_OPERATOR_LIMIT = 8
    }
}