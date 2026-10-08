package com.example.gash.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gash.core.locale.AppLanguage
import com.example.gash.domain.repository.TagSettings
import com.example.gash.domain.usecase.language.GetCurrentLanguageUseCase
import com.example.gash.domain.usecase.language.SetLanguageUseCase
import com.example.gash.domain.usecase.settings.ObserveTagSettingsUseCase
import com.example.gash.domain.usecase.settings.SetTagEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getCurrentLanguageUseCase: GetCurrentLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    observeTagSettingsUseCase: ObserveTagSettingsUseCase,
    private val setTagEnabledUseCase: SetTagEnabledUseCase
) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(getCurrentLanguageUseCase())
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    val tagSettings: StateFlow<TagSettings> = observeTagSettingsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TagSettings(false, false, false))

    fun onLanguageSelected(language: AppLanguage) {
        setLanguageUseCase(language)
        _currentLanguage.value = language
    }

    fun onTagToggle(tagNumber: Int, enabled: Boolean) {
        viewModelScope.launch { setTagEnabledUseCase(tagNumber, enabled) }
    }
}