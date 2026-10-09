package com.example.gash.data.repository

import android.util.Log
import com.example.gash.BuildConfig
import com.example.gash.core.datastore.ActivationPreferences
import com.example.gash.core.storage.ProfileImageStorage
import com.example.gash.domain.error.DomainError
import com.example.gash.domain.repository.ProfileImageRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ProfileImageRepositoryImpl @Inject constructor(
    private val prefs: ActivationPreferences,
    private val storage: ProfileImageStorage
) : ProfileImageRepository {

    override val profileImagePath: Flow<String?> = prefs.profileImagePath

    override suspend fun setProfileImage(sourceUri: String): Result<Unit> {
        return try {
            val oldPath = prefs.profileImagePath.first()
            val newPath = storage.saveFromUri(sourceUri)
            prefs.setProfileImagePath(newPath)
            oldPath?.let(storage::delete)
            Result.success(Unit)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) Log.w("ProfileImage", "setProfileImage failed", e)
            Result.failure(DomainError.ProfileImageFailed(e))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(DomainError.ProfileImageFailed(e))
        }
    }

    override suspend fun removeProfileImage() {
        val oldPath = prefs.profileImagePath.first()
        prefs.setProfileImagePath(null)
        oldPath?.let(storage::delete)
    }
}