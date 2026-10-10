package com.example.gash.domain.usecase.animal

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.model.AnimalTags
import com.example.gash.domain.repository.AnimalRepository
import com.example.gash.domain.repository.RfidTagRepository
import com.example.gash.domain.transaction.TransactionRunner
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class RegisterAnimalUseCase @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val rfidTagRepository: RfidTagRepository,
    private val transactionRunner: TransactionRunner
) {
    suspend operator fun invoke(
        herdId: Long?,
        rfidCode: String?,
        tags: AnimalTags = AnimalTags.EMPTY
    ): Result<Long> {
        return try {
            val animalId = transactionRunner.run {
                val id = animalRepository.registerAnimal(herdId, tags.normalized())

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
        } catch (e: CancellationException) {
            throw e
        } catch (e: DomainError) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(DomainError.Unknown(e))
        }
    }
}