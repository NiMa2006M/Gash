// SetTagNameUseCase.kt  (جدید)
package com.example.gash.domain.usecase.settings

import com.example.gash.domain.model.TagSettings
import com.example.gash.domain.repository.TagSettingsRepository
import javax.inject.Inject

class SetTagNameUseCase @Inject constructor(
    private val repository: TagSettingsRepository
) {
    suspend operator fun invoke(slot: Int, name: String) =
        repository.setTagName(slot, name.trim().take(TagSettings.MAX_NAME_LENGTH))
}