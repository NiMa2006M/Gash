package com.example.gash.core.locale

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import javax.inject.Inject

class AppCompatLanguageProvider @Inject constructor() : LanguageProvider {

    override fun getCurrentLanguage(): AppLanguage =
        AppLanguage.fromTag(AppCompatDelegate.getApplicationLocales().toLanguageTags())

    override fun setLanguage(language: AppLanguage) {
        val locales = if (language == AppLanguage.SYSTEM) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(language.tag)
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }
}