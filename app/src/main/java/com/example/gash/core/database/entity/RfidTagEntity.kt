package com.example.gash.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rfid_tags",
    foreignKeys = [
        ForeignKey(
            entity = AnimalEntity::class,
            parentColumns = ["id"],
            childColumns = ["animalId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(
            value = ["code"],
            unique = true
        ),
        Index("animalId")

    ])
data class RfidTagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String,
    val animalId: Long?,
    val isActive: Boolean,
    val assignedAt: Long,
    val unassignedAt: Long? = null
)