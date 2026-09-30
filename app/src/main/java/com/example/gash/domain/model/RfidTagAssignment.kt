package com.example.gash.domain.model

data class RfidTagAssignment(
    val id: Long,
    val code: String,
    val animalId: Long?,
    val isActive: Boolean,
    val assignedAt: Long,
    val unassignedAt: Long?
)