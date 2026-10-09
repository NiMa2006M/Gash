package com.example.gash.core.locale

import com.example.gash.R

enum class AppLanguage(val tag: String, val labelRes: Int) {
    SYSTEM("", R.string.settings_language_system),
    PERSIAN("fa", R.string.settings_language_fa),
    ENGLISH("en", R.string.settings_language_en);

    companion object {
        fun fromTag(tag: String): AppLanguage {
            val language = tag.substringBefore(',').substringBefore('-')
            return entries.firstOrNull { it.tag.isNotEmpty() && it.tag == language } ?: SYSTEM
        }
    }
}