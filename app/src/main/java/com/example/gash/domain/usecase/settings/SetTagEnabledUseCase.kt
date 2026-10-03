package com.example.gash.domain.usecase.settings

import com.example.gash.domain.repository.TagSettingsRepository
import javax.inject.Inject

class SetTagEnabledUseCase @Inject constructor(
    private val repository: TagSettingsRepository
) {
    suspend operator fun invoke(tagNumber: Int, enabled: Boolean) =
        repository.setTagEnabled(tagNumber, enabled)
}