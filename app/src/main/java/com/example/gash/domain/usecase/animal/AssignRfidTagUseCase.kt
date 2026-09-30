package com.example.gash.domain.usecase.animal

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.repository.RfidTagRepository
import javax.inject.Inject

class AssignRfidTagUseCase @Inject constructor(
    private val rfidTagRepository: RfidTagRepository
) {
    suspend operator fun invoke(rfidCode: String, animalId: Long): Result<Unit> {
        if (rfidCode.isBlank()) {
            return Result.failure(DomainError.EmptyRfidCode)
        }
        return rfidTagRepository.assignTagToAnimal(rfidCode.trim(), animalId)
    }
}