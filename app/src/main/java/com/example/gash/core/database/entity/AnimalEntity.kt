package com.example.gash.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "animals",
    foreignKeys = [
        ForeignKey(
            entity = HerdEntity::class,
            parentColumns = ["id"],
            childColumns = ["herdId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("herdId")]
)
data class AnimalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val herdId: Long?,
    val createdAt: Long,
    val expiredAt : Long?
)
