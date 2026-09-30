package com.example.gash.domain.repository

import com.example.gash.domain.model.RfidTagAssignment

interface RfidTagRepository {

    suspend fun findActiveByCode(code: String): RfidTagAssignment?

    suspend fun assignTagToAnimal(
        code: String,
        animalId: Long
    ): Result<Unit>

    suspend fun unassignTag(tagId: Long)
}