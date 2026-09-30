package com.example.gash.core.locale

import com.example.gash.R

enum class AppLanguage(val tag: String, val labelRes: Int) {
    SYSTEM("", R.string.settings_language_system),
    PERSIAN("fa", R.string.settings_language_fa),
    ENGLISH("en", R.string.settings_language_en);

    companion object {
        fun fromTag(tag: String): AppLanguage =
            entries.firstOrNull { it.tag == tag } ?: SYSTEM
    }
}