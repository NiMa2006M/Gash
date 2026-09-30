package com.example.gash.domain.usecase.activation

import com.example.gash.domain.repository.ActivationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFarmNameUseCase @Inject constructor(
    private val activationRepository: ActivationRepository
) {
    operator fun invoke(): Flow<String?> = activationRepository.farmName
}