package com.example.gash.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vaccination_records",
    foreignKeys = [
        ForeignKey(
            entity = VaccinationOperationEntity::class,
            parentColumns = ["id"],
            childColumns = ["operationId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AnimalEntity::class,
            parentColumns = ["id"],
            childColumns = ["animalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["operationId", "animalId"], unique = true),
        Index("animalId")
    ]
)
data class VaccinationRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val operationId: Long,
    val animalId: Long,
    val scannedRfidCode: String?,
    val recordedAt: Long
)