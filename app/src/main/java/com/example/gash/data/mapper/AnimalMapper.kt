package com.example.gash.data.mapper

import com.example.gash.core.database.entity.AnimalEntity
import com.example.gash.domain.model.Animal

fun AnimalEntity.toDomain(activeRfidCode: String? = null): Animal = Animal(
    id = id,
    herdId = herdId,
    createdAt = createdAt,
    activeRfidCode = activeRfidCode,
    expiredAt = expiredAt
)