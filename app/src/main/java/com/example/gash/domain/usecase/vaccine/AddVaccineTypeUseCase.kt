package com.example.gash.domain.usecase.vaccine

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.repository.VaccineTypeRepository
import javax.inject.Inject

class AddVaccineTypeUseCase @Inject constructor(
    private val repository: VaccineTypeRepository
) {
    suspend operator fun invoke(name: String): Result<Long> {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return Result.failure(DomainError.EmptyVaccineName)

        val existing = repository.findByName(trimmed)
        return when {
            existing == null -> Result.success(repository.add(trimmed))
            existing.isArchived -> {
                repository.restore(existing.id, trimmed)
                Result.success(existing.id)
            }
            else -> Result.failure(DomainError.DuplicateVaccineName)
        }
    }
}