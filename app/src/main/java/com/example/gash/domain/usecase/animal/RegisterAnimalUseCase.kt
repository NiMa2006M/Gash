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
            val animalId = database.withTransaction {
                val id = animalRepository.registerAnimal(herdId)

                if (!rfidCode.isNullOrBlank()) {
                    val assignResult = rfidTagRepository.assignTagToAnimal(rfidCode.trim(), id)
                    if (assignResult.isFailure) {
                        val cause = assignResult.exceptionOrNull()
                        throw cause as? DomainError ?: DomainError.RfidAssignmentFailed(cause)
                    }
                }

                id
            }
            Result.success(animalId)
        } catch (e: DomainError) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(DomainError.Unknown(e))
        }
    }
}