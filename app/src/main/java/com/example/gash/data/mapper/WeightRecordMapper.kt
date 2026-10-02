package com.example.gash.data.mapper

import com.example.gash.core.database.entity.WeightRecordEntity
import com.example.gash.domain.model.WeightRecord

fun WeightRecordEntity.toDomain(): WeightRecord = WeightRecord(
    id = id,
    animalId = animalId,
    weightKg = weightKg,
    recordedAt = recordedAt
)