package com.example.gash.data.mapper

import com.example.gash.core.database.entity.HerdEntity
import com.example.gash.domain.model.Herd

fun HerdEntity.toDomain(animalCount: Int = 0): Herd = Herd(
    id = id,
    name = name,
    createdAt = createdAt,
    animalCount = animalCount
)
