package com.example.gash.domain.repository

import kotlinx.coroutines.flow.Flow

interface ProfileImageRepository {
    val profileImagePath: Flow<String?>

    suspend fun setProfileImage(sourceUri: String): Result<Unit>

    suspend fun removeProfileImage()
}