package com.example.gash.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vaccination_operations",
    foreignKeys = [
        ForeignKey(
            entity = VaccineTypeEntity::class,
            parentColumns = ["id"],
            childColumns = ["vaccineTypeId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = HerdEntity::class,
            parentColumns = ["id"],
            childColumns = ["herdId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("vaccineTypeId"), Index("herdId"), Index("finishedAt")]
)
data class VaccinationOperationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vaccineTypeId: Long,
    val injectionSite: String,
    val doseMl: Double?,
    val operatorName: String? = null,
    val herdId: Long?,
    val herdNameSnapshot: String?,
    val startedAt: Long,
    val finishedAt: Long? = null
)