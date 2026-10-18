package com.example.gash.domain.model

data class VaccinationRecord(
    val id: Long,
    val operationId: Long,
    val animalId: Long,
    val scannedRfidCode: String?,
    val recordedAt: Long
)