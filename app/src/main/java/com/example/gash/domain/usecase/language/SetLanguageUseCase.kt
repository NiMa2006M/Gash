package com.example.gash.domain.usecase.language

import com.example.gash.core.locale.AppLanguage
import com.example.gash.core.locale.LanguageProvider
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val languageProvider: LanguageProvider
) {
    operator fun invoke(language: AppLanguage) = languageProvider.setLanguage(language)
}