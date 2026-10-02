package com.example.gash.domain.model

data class WeightRecord(
    val id: Long,
    val animalId: Long,
    val weightKg: Double,
    val recordedAt: Long
)