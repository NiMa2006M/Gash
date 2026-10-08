package com.example.gash.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gash.domain.repository.ActivationRepository
import com.example.gash.domain.usecase.activation.UpdateFarmInfoUseCase
import com.example.gash.ui.helper.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralInfoViewModel @Inject constructor(
    private val activationRepository: ActivationRepository,
    private val updateFarmInfoUseCase: UpdateFarmInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GeneralInfoUiState())
    val uiState: StateFlow<GeneralInfoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    farmName = activationRepository.farmName.first().orEmpty(),
                    farmId = activationRepository.farmId.first().orEmpty(),
                    phoneNumber = activationRepository.phoneNumber.first().orEmpty()
                )
            }
        }
    }

    fun onFarmNameChange(value: String) = _uiState.update { it.copy(farmName = value, isSaved = false) }
    fun onFarmIdChange(value: String) = _uiState.update { it.copy(farmId = value, isSaved = false) }
    fun onPhoneNumberChange(value: String) = _uiState.update { it.copy(phoneNumber = value, isSaved = false) }

    fun onSaveClick() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            val result = updateFarmInfoUseCase(state.farmName, state.farmId, state.phoneNumber)
            _uiState.update {
                if (result.isSuccess) it.copy(isSubmitting = false, isSaved = true, error = null)
                else it.copy(isSubmitting = false, error = result.exceptionOrNull()?.toUiText())
            }
        }
    }
}