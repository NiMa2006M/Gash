package com.example.gash.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vaccine_types",
    indices = [Index(value = ["normalizedName"], unique = true)]
)
data class VaccineTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val normalizedName: String,
    val isArchived: Boolean = false,
    val createdAt: Long
)