package com.example.gash.data.mapper

import com.example.gash.core.database.entity.RfidTagEntity
import com.example.gash.domain.model.RfidTagAssignment

fun RfidTagEntity.toDomain(): RfidTagAssignment = RfidTagAssignment(
    id = id,
    code = code,
    animalId = animalId,
    isActive = isActive,
    assignedAt = assignedAt,
    unassignedAt = unassignedAt
)