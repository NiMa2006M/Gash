package com.example.gash.domain.repository

import kotlinx.coroutines.flow.Flow

data class TagSettings(
    val isTag1Enabled: Boolean,
    val isTag2Enabled: Boolean,
    val isTag3Enabled: Boolean
)

interface TagSettingsRepository {
    val settings: Flow<TagSettings>
    suspend fun setTagEnabled(tagNumber: Int, enabled: Boolean)
}