package com.example.gash.domain.usecase.settings

import com.example.gash.domain.repository.TagSettings
import com.example.gash.domain.repository.TagSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTagSettingsUseCase @Inject constructor(
    private val repository: TagSettingsRepository
) {
    operator fun invoke(): Flow<TagSettings> = repository.settings
}