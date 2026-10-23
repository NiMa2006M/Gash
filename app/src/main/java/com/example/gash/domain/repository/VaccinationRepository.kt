package com.example.gash.domain.repository

import com.example.gash.domain.model.VaccinationOperation
import com.example.gash.domain.model.VaccinationOperationSummary
import com.example.gash.domain.model.VaccinationRecord
import com.example.gash.domain.model.VaccinationSettings
import kotlinx.coroutines.flow.Flow

interface VaccinationRepository {
    suspend fun getOpenOperation(): VaccinationOperation?
    suspend fun getOperation(operationId: Long): VaccinationOperation?
    suspend fun createOperation(settings: VaccinationSettings): Long
    suspend fun finishOperation(operationId: Long)
    suspend fun deleteOperation(operationId: Long)
    fun observeRecords(operationId: Long): Flow<List<VaccinationRecord>>
    suspend fun getRecord(recordId: Long): VaccinationRecord?
    suspend fun hasRecord(operationId: Long, animalId: Long): Boolean
    suspend fun addRecord(operationId: Long, animalId: Long, scannedRfidCode: String?): Long
    suspend fun deleteRecord(recordId: Long)
    suspend fun countRecords(operationId: Long): Int
    fun observeRecentOperatorNames(): Flow<List<String>>
    fun observeOperationSummaries(): Flow<List<VaccinationOperationSummary>>
}