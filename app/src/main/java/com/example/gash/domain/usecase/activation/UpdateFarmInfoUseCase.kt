package com.example.gash.domain.usecase.activation

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.repository.ActivationRepository
import javax.inject.Inject

class UpdateFarmInfoUseCase @Inject constructor(
    private val repository: ActivationRepository
) {
    suspend operator fun invoke(farmName: String, farmId: String, phoneNumber: String): Result<Unit> {
        if (farmName.isBlank()) {
            return Result.failure(DomainError.EmptyFarmName)
        }
        repository.saveActivation(farmName.trim(), farmId.trim(), phoneNumber.trim())
        return Result.success(Unit)
    }
}