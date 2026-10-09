package com.example.gash.domain.repository

import com.example.gash.domain.model.TagSettings
import kotlinx.coroutines.flow.Flow

interface TagSettingsRepository {
    val settings: Flow<TagSettings>
    suspend fun setTagEnabled(slot: Int, enabled: Boolean)
    suspend fun setTagName(slot: Int, name: String)
}