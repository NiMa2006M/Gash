package com.example.gash.data.repository

import androidx.room.withTransaction
import com.example.gash.core.database.AppDatabase
import com.example.gash.core.database.dao.AnimalDao
import com.example.gash.core.database.dao.RfidTagDao
import com.example.gash.core.database.entity.RfidTagEntity
import com.example.gash.data.mapper.toDomain
import com.example.gash.domain.error.DomainError
import com.example.gash.domain.model.RfidTagAssignment
import com.example.gash.domain.repository.RfidTagRepository
import javax.inject.Inject

class RfidTagRepositoryImpl @Inject constructor(
    private val rfidTagDao: RfidTagDao,
    private val animalDao: AnimalDao,
    private val database: AppDatabase
) : RfidTagRepository {

    override suspend fun findActiveByCode(code: String): RfidTagAssignment? =
        rfidTagDao.getActiveByCode(code)?.toDomain()

    override suspend fun assignTagToAnimal(code: String, animalId: Long): Result<Unit> {
        return try {
            database.withTransaction {
                val animal = animalDao.getById(animalId)
                    ?: return@withTransaction Result.failure<Unit>(DomainError.AnimalNotFound)

                if (animal.expiredAt != null) {
                    return@withTransaction Result.failure<Unit>(DomainError.AnimalExpired)
                }

                val now = System.currentTimeMillis()

                val activeByCode = rfidTagDao.getActiveByCode(code)
                val activeByAnimal = rfidTagDao.getActiveByAnimal(animalId)

                if (activeByCode != null && activeByCode.animalId == animalId) {
                    return@withTransaction Result.success(Unit)
                }

                if (activeByCode != null) {
                    rfidTagDao.deactivate(id = activeByCode.id, unassignedAt = now)
                }

                if (activeByAnimal != null) {
                    rfidTagDao.deactivate(id = activeByAnimal.id, unassignedAt = now)
                }

                rfidTagDao.insert(
                    RfidTagEntity(
                        code = code,
                        animalId = animalId,
                        isActive = true,
                        assignedAt = now
                    )
                )

                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(DomainError.RfidAssignmentFailed(e))
        }
    }

    override suspend fun unassignTag(tagId: Long) {
        rfidTagDao.deactivate(tagId, System.currentTimeMillis())
    }
}