package com.example.gash.data.repository

import com.example.gash.core.datastore.TagSettingsPreferences
import com.example.gash.domain.model.TagConfig
import com.example.gash.domain.model.TagSettings
import com.example.gash.domain.repository.TagSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TagSettingsRepositoryImpl @Inject constructor(
    private val prefs: TagSettingsPreferences
) : TagSettingsRepository {

    override val settings: Flow<TagSettings> =
        prefs.observeTags(1..TagSettings.SLOT_COUNT).map { stored ->
            TagSettings(stored.map { TagConfig(it.slot, it.isEnabled, it.name) })
        }

    override suspend fun setTagEnabled(slot: Int, enabled: Boolean) {
        requireValidSlot(slot)
        prefs.setEnabled(slot, enabled)
    }

    override suspend fun setTagName(slot: Int, name: String) {
        requireValidSlot(slot)
        prefs.setName(slot, name)
    }

    private fun requireValidSlot(slot: Int) =
        require(slot in 1..TagSettings.SLOT_COUNT) { "Invalid tag slot: $slot" }
}