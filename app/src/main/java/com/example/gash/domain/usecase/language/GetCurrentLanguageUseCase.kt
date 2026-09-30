package com.example.gash.domain.usecase.language

import com.example.gash.core.locale.AppLanguage
import com.example.gash.core.locale.LanguageProvider
import javax.inject.Inject

class GetCurrentLanguageUseCase @Inject constructor(
    private val languageProvider: LanguageProvider
) {
    operator fun invoke(): AppLanguage = languageProvider.getCurrentLanguage()
}