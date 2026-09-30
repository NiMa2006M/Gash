package com.example.gash.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "herds")
data class HerdEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val createdAt: Long
)