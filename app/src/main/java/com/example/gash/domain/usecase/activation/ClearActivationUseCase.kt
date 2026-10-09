package com.example.gash.domain.usecase.activation

import com.example.gash.domain.repository.ActivationRepository
import com.example.gash.domain.repository.ProfileImageRepository
import javax.inject.Inject

class ClearActivationUseCase @Inject constructor(
    private val activationRepository: ActivationRepository,
    private val profileImageRepository: ProfileImageRepository
) {
    suspend operator fun invoke() {
        profileImageRepository.removeProfileImage()
        activationRepository.clearActivation()
    }
}