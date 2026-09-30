package com.example.gash.core.locale

interface LanguageProvider {
    fun getCurrentLanguage(): AppLanguage
    fun setLanguage(language: AppLanguage)
}