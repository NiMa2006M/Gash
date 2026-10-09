package com.example.gash.domain.usecase.profile

import com.example.gash.domain.repository.ProfileImageRepository
import javax.inject.Inject

class SetProfileImageUseCase @Inject constructor(
    private val repository: ProfileImageRepository
) {
    suspend operator fun invoke(sourceUri: String): Result<Unit> = repository.setProfileImage(sourceUri)
}