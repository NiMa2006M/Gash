package com.example.gash.domain.usecase.herd

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.repository.HerdRepository
import javax.inject.Inject

class AddHerdUseCase @Inject constructor(
    private val repository: HerdRepository
) {
    suspend operator fun invoke(name: String): Result<Long> {
        if (name.isBlank()) {
            return Result.failure(DomainError.EmptyHerdName)
        }
        return Result.success(repository.addHerd(name.trim()))
    }
}