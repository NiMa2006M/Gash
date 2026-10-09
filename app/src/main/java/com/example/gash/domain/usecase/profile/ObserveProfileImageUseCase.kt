package com.example.gash.domain.usecase.profile

import com.example.gash.domain.repository.ProfileImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveProfileImageUseCase @Inject constructor(
    private val repository: ProfileImageRepository
) {
    operator fun invoke(): Flow<String?> = repository.profileImagePath
}