package com.example.gash.domain.usecase.animal

import androidx.room.withTransaction
import com.example.gash.core.database.AppDatabase
import com.example.gash.domain.error.DomainError
import com.example.gash.domain.repository.AnimalRepository
import com.example.gash.domain.repository.RfidTagRepository
import javax.inject.Inject

class RegisterAnimalUseCase @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val rfidTagRepository: RfidTagRepository,
    private val database: AppDatabase
) {
    suspend operator fun invoke(herdId: Long?, rfidCode: String?): Result<Long> {
        return try {
            database.withTransaction {
                val animalId = animalRepository.registerAnimal(herdId)

                if (!rfidCode.isNullOrBlank()) {
                    val assignResult = rfidTagRepository.assignTagToAnimal(rfidCode.trim(), animalId)
                    if (assignResult.isFailure) {
                        val cause = assignResult.exceptionOrNull()
                        val error = cause as? DomainError ?: DomainError.RfidAssignmentFailed(cause)
                        return@withTransaction Result.failure(error)
                    }
                }
                return@withTransaction Result.success(animalId)
            }

        } catch (e: Exception) {
            Result.failure(DomainError.RfidAssignmentFailed(e))
        }

    }
}