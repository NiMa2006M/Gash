package com.example.gash.domain.model

data class TagSettings(val tags: List<TagConfig>) {

    val enabledTags: List<TagConfig> get() = tags.filter { it.isEnabled }

    companion object {
        const val SLOT_COUNT = 3
        const val MAX_NAME_LENGTH = 24
    }
}