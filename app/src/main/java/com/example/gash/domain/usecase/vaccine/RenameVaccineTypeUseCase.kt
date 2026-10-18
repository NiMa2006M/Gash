package com.example.gash.domain.usecase.vaccine

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.repository.VaccineTypeRepository
import javax.inject.Inject

class RenameVaccineTypeUseCase @Inject constructor(
    private val repository: VaccineTypeRepository
) {
    suspend operator fun invoke(id: Long, name: String): Result<Unit> {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return Result.failure(DomainError.EmptyVaccineName)

        val existing = repository.findByName(trimmed)
        if (existing != null && existing.id != id) {
            return Result.failure(DomainError.DuplicateVaccineName)
        }

        repository.rename(id, trimmed)
        return Result.success(Unit)
    }
}