package com.example.gash.core.locale

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import javax.inject.Inject
import kotlin.math.log

class AppCompatLanguageProvider @Inject constructor() : LanguageProvider {
    private val tag2 = "LanguageDebug"

    override fun getCurrentLanguage(): AppLanguage {
        val tag = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        return AppLanguage.fromTag(tag)
    }

    override fun setLanguage(language: AppLanguage) {
        Log.d(tag2, AppLanguage.SYSTEM.toString())
        val locales = if (language == AppLanguage.SYSTEM) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(language.tag)
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }
}