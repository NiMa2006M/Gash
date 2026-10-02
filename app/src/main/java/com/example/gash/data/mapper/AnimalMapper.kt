package com.example.gash.data.mapper

import com.example.gash.core.database.entity.AnimalEntity
import com.example.gash.domain.model.Animal

fun AnimalEntity.toDomain(activeRfidCode: String? = null): Animal = Animal(
    id = id,
    herdId = herdId,
    createdAt = createdAt,
    expiredAt = expiredAt,
    activeRfidCode = activeRfidCode,
    tag1 = tag1,
    tag2 = tag2,
    tag3 = tag3
)