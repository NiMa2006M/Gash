package com.example.gash.core.database.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import com.example.gash.core.database.entity.VaccinationOperationEntity
import com.example.gash.core.database.entity.VaccinationRecordEntity
import kotlinx.coroutines.flow.Flow

data class VaccinationOperationRow(
    @Embedded val operation: VaccinationOperationEntity,
    val vaccineTypeName: String
)

@Dao
interface VaccinationDao {

    @Insert
    suspend fun insertOperation(operation: VaccinationOperationEntity): Long

    @Query(
        """
        SELECT op.*, vt.name AS vaccineTypeName
        FROM vaccination_operations op
        INNER JOIN vaccine_types vt ON vt.id = op.vaccineTypeId
        WHERE op.finishedAt IS NULL
        ORDER BY op.id DESC
        LIMIT 1
        """
    )
    suspend fun getOpenOperation(): VaccinationOperationRow?

    @Query(
        """
        SELECT op.*, vt.name AS vaccineTypeName
        FROM vaccination_operations op
        INNER JOIN vaccine_types vt ON vt.id = op.vaccineTypeId
        WHERE op.id = :operationId
        """
    )
    suspend fun getOperation(operationId: Long): VaccinationOperationRow?

    @Query("UPDATE vaccination_operations SET finishedAt = :finishedAt WHERE id = :operationId")
    suspend fun finishOperation(operationId: Long, finishedAt: Long)

    @Query("DELETE FROM vaccination_operations WHERE id = :operationId")
    suspend fun deleteOperation(operationId: Long)

    @Insert
    suspend fun insertRecord(record: VaccinationRecordEntity): Long

    @Query("SELECT * FROM vaccination_records WHERE id = :recordId")
    suspend fun getRecord(recordId: Long): VaccinationRecordEntity?

    @Query("DELETE FROM vaccination_records WHERE id = :recordId")
    suspend fun deleteRecord(recordId: Long)

    @Query("SELECT COUNT(*) FROM vaccination_records WHERE operationId = :operationId AND animalId = :animalId")
    suspend fun countRecordsForAnimal(operationId: Long, animalId: Long): Int

    @Query("SELECT COUNT(*) FROM vaccination_records WHERE operationId = :operationId")
    suspend fun countRecords(operationId: Long): Int

    @Query("SELECT * FROM vaccination_records WHERE operationId = :operationId ORDER BY recordedAt DESC, id DESC")
    fun observeRecords(operationId: Long): Flow<List<VaccinationRecordEntity>>

    @Query(
        """
    SELECT operatorName FROM vaccination_operations
    WHERE operatorName IS NOT NULL AND operatorName != ''
    GROUP BY operatorName
    ORDER BY MAX(startedAt) DESC
    LIMIT :limit
    """
    )
    fun observeRecentOperatorNames(limit: Int): Flow<List<String>>
}