package com.example.gash.domain.usecase.profile

import com.example.gash.domain.repository.ProfileImageRepository
import javax.inject.Inject

class RemoveProfileImageUseCase @Inject constructor(
    private val repository: ProfileImageRepository
) {
    suspend operator fun invoke() = repository.removeProfileImage()
}