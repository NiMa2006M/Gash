package com.example.gash.core.database.dao

import androidx.room.Dao
import androidx.room.Query

data class AnimalWeightExportRow(
    val animalId: Long,
    val rfidCode: String?,
    val herdName: String?,
    val tag1: String?,
    val tag2: String?,
    val tag3: String?,
    val createdAt: Long,
    val lastWeightKg: Double?,
    val lastWeightAt: Long?
)

data class WeightHistoryExportRow(
    val animalId: Long,
    val rfidCode: String?,
    val weightKg: Double,
    val recordedAt: Long
)

data class VaccinationExportRow(
    val recordedAt: Long,
    val animalId: Long,
    val scannedRfidCode: String?,
    val animalHerdName: String?
)

@Dao
interface ExportDao {

    @Query(
        """
        SELECT a.id AS animalId, r.code AS rfidCode, h.name AS herdName,
               a.tag1 AS tag1, a.tag2 AS tag2, a.tag3 AS tag3, a.createdAt AS createdAt,
               w.weightKg AS lastWeightKg, w.recordedAt AS lastWeightAt
        FROM animals a
        LEFT JOIN herds h ON h.id = a.herdId
        LEFT JOIN rfid_tags r ON r.animalId = a.id AND r.isActive = 1
        LEFT JOIN weight_records w ON w.id = (
            SELECT w2.id FROM weight_records w2
            WHERE w2.animalId = a.id
            ORDER BY w2.recordedAt DESC, w2.id DESC
            LIMIT 1
        )
        WHERE a.expiredAt IS NULL
        ORDER BY a.id ASC
        """
    )
    suspend fun getAnimalWeightRows(): List<AnimalWeightExportRow>

    @Query(
        """
        SELECT w.animalId AS animalId, r.code AS rfidCode, w.weightKg AS weightKg, w.recordedAt AS recordedAt
        FROM weight_records w
        INNER JOIN animals a ON a.id = w.animalId
        LEFT JOIN rfid_tags r ON r.animalId = w.animalId AND r.isActive = 1
        WHERE a.expiredAt IS NULL
        ORDER BY w.animalId ASC, w.recordedAt ASC, w.id ASC
        """
    )
    suspend fun getWeightHistoryRows(): List<WeightHistoryExportRow>

    @Query(
        """
        SELECT r.recordedAt AS recordedAt, r.animalId AS animalId,
               r.scannedRfidCode AS scannedRfidCode, h.name AS animalHerdName
        FROM vaccination_records r
        INNER JOIN animals a ON a.id = r.animalId
        LEFT JOIN herds h ON h.id = a.herdId
        WHERE r.operationId = :operationId
        ORDER BY r.recordedAt ASC, r.id ASC
        """
    )
    suspend fun getVaccinationRows(operationId: Long): List<VaccinationExportRow>
}