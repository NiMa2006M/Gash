package com.example.gash.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gash.domain.usecase.activation.ClearActivationUseCase
import com.example.gash.domain.usecase.activation.ObserveFarmNameUseCase
import com.example.gash.domain.usecase.profile.ObserveProfileImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    observeFarmNameUseCase: ObserveFarmNameUseCase,
    observeProfileImageUseCase: ObserveProfileImageUseCase,
    private val clearActivationUseCase: ClearActivationUseCase
) : ViewModel() {

    val farmName: StateFlow<String?> = observeFarmNameUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val profileImagePath: StateFlow<String?> = observeProfileImageUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun onLogout() {
        viewModelScope.launch { clearActivationUseCase() }
    }
}