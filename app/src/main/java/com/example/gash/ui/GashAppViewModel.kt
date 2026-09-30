package com.example.gash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gash.domain.repository.ActivationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GashAppViewModel @Inject constructor(
    activationRepository: ActivationRepository
) : ViewModel() {
    val isActivated: StateFlow<Boolean?> = activationRepository.isActivated
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}