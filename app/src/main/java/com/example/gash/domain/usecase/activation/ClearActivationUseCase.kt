package com.example.gash.domain.usecase.activation

import com.example.gash.domain.repository.ActivationRepository
import javax.inject.Inject

class ClearActivationUseCase @Inject constructor(
    private val repository: ActivationRepository
) {
    suspend operator fun invoke() = repository.clearActivation()
}