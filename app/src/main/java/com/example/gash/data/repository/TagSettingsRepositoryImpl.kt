package com.example.gash.data.repository

import com.example.gash.core.datastore.TagSettingsPreferences
import com.example.gash.domain.repository.TagSettings
import com.example.gash.domain.repository.TagSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class TagSettingsRepositoryImpl @Inject constructor(
    private val prefs: TagSettingsPreferences
) : TagSettingsRepository {

    override val settings: Flow<TagSettings> = combine(
        prefs.isTag1Enabled, prefs.isTag2Enabled, prefs.isTag3Enabled
    ) { t1, t2, t3 -> TagSettings(t1, t2, t3) }

    override suspend fun setTagEnabled(tagNumber: Int, enabled: Boolean) {
        when (tagNumber) {
            1 -> prefs.setTag1Enabled(enabled)
            2 -> prefs.setTag2Enabled(enabled)
            3 -> prefs.setTag3Enabled(enabled)
            else -> throw IllegalArgumentException("Invalid tag number: $tagNumber")
        }
    }
}