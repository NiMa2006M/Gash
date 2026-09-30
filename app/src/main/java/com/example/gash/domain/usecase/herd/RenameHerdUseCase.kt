package com.example.gash.domain.usecase.herd

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.repository.HerdRepository
import javax.inject.Inject

class RenameHerdUseCase @Inject constructor(
    private val repository: HerdRepository
) {
    suspend operator fun invoke(herdId: Long, newName: String): Result<Unit> {
        if (newName.isBlank()) {
            return Result.failure(DomainError.EmptyHerdName)
        }
        repository.renameHerd(herdId, newName.trim())
        return Result.success(Unit)
    }
}