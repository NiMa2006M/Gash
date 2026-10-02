package com.example.gash.domain.usecase.weight

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.repository.WeightRecordRepository
import javax.inject.Inject

class RecordWeightUseCase @Inject constructor(
    private val repository: WeightRecordRepository
) {
    suspend operator fun invoke(animalId: Long, weightKg: Double): Result<Unit> {
        if (weightKg <= 0.0) {
            return Result.failure(DomainError.InvalidWeight)
        }
        repository.recordWeight(animalId, weightKg)
        return Result.success(Unit)
    }
}