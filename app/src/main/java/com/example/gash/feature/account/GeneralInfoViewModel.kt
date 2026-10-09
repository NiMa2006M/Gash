package com.example.gash.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gash.domain.error.DomainError
import com.example.gash.domain.repository.ActivationRepository
import com.example.gash.domain.usecase.activation.UpdateFarmInfoUseCase
import com.example.gash.domain.usecase.profile.ObserveProfileImageUseCase
import com.example.gash.domain.usecase.profile.RemoveProfileImageUseCase
import com.example.gash.domain.usecase.profile.SetProfileImageUseCase
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
    private val updateFarmInfoUseCase: UpdateFarmInfoUseCase,
    observeProfileImageUseCase: ObserveProfileImageUseCase,
    private val setProfileImageUseCase: SetProfileImageUseCase,
    private val removeProfileImageUseCase: RemoveProfileImageUseCase
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
        viewModelScope.launch {
            observeProfileImageUseCase().collect { path ->
                _uiState.update { it.copy(profileImagePath = path) }
            }
        }
    }

    fun onFarmNameChange(value: String) =
        _uiState.update { it.copy(farmName = value, farmNameError = false, isSaved = false) }

    fun onFarmIdChange(value: String) =
        _uiState.update { it.copy(farmId = value, farmIdError = false, isSaved = false) }

    fun onPhoneNumberChange(value: String) =
        _uiState.update { it.copy(phoneNumber = value, phoneNumberError = false, isSaved = false) }

    fun onSaveClick() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            val result = updateFarmInfoUseCase(state.farmName, state.farmId, state.phoneNumber)
            val failure = result.exceptionOrNull()

            _uiState.update {
                when {
                    result.isSuccess -> it.copy(
                        isSubmitting = false,
                        isSaved = true,
                        error = null,
                        farmNameError = false,
                        farmIdError = false,
                        phoneNumberError = false
                    )
                    failure is DomainError.InvalidFarmInfo -> it.copy(
                        isSubmitting = false,
                        isSaved = false,
                        error = null,
                        farmNameError = failure.farmNameEmpty,
                        farmIdError = failure.farmIdEmpty,
                        phoneNumberError = failure.phoneNumberEmpty
                    )
                    else -> it.copy(isSubmitting = false, error = failure?.toUiText())
                }
            }
        }
    }

    fun onProfileImagePicked(sourceUri: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isImageProcessing = true, error = null) }
            val result = setProfileImageUseCase(sourceUri)
            _uiState.update {
                it.copy(
                    isImageProcessing = false,
                    error = result.exceptionOrNull()?.toUiText()
                )
            }
        }
    }

    fun onRemoveProfileImage() {
        viewModelScope.launch { removeProfileImageUseCase() }
    }
}