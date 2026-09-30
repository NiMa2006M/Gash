package com.example.gash.feature.activation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gash.R
import com.example.gash.domain.usecase.activation.ActivateDeviceUseCase
import com.example.gash.ui.helper.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ActivationViewModel @Inject constructor(
    private val activateDeviceUseCase: ActivateDeviceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActivationUiState())
    val uiState: StateFlow<ActivationUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ActivationEvent>()
    val events: SharedFlow<ActivationEvent> = _events.asSharedFlow()

    private var countdownJob: Job? = null

    fun onFarmNameChange(value: String) = _uiState.update { it.copy(farmName = value, formError = null) }
    fun onFarmIdChange(value: String) = _uiState.update { it.copy(farmId = value, formError = null) }
    fun onPhoneNumberChange(value: String) = _uiState.update { it.copy(phoneNumber = value, formError = null) }


    fun onRequestCode() {
        val s = _uiState.value
        if (s.farmName.isBlank() || s.farmId.isBlank() || s.phoneNumber.isBlank()) {
            _uiState.update { it.copy(formError = UiText.StringResource(R.string.activation_form_fillAllFields)) }
            return
        }
        _uiState.update {
            it.copy(step = ActivationStep.CodeEntry, formError = null, isCodeError = false, enteredCode = "")
        }
        startCountdown()
    }

    fun onEditClick() {
        countdownJob?.cancel()
        _uiState.update { it.copy(step = ActivationStep.Form) }
    }

    fun onCodeChange(value: String) {
        val digits = value.filter { it.isDigit() }.take(CODE_LENGTH)
        _uiState.update { it.copy(enteredCode = digits, isCodeError = false) }
    }

    fun onResendCode() {
        _uiState.update {
            it.copy(
                isCodeError = false,
                enteredCode = ""
            )
        }

        startCountdown()
    }

    fun onSubmitCode() {
        val s = _uiState.value
        if (s.enteredCode.length != CODE_LENGTH || s.isSubmitting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            val result = activateDeviceUseCase(
                farmName = s.farmName,
                farmId = s.farmId,
                phoneNumber = s.phoneNumber,
                enteredCode = s.enteredCode
            )
            _uiState.update { it.copy(isSubmitting = false) }
            if (result.isSuccess) {
                _events.emit(ActivationEvent.NavigateToHome)
            } else {
                _uiState.update { it.copy(isCodeError = true) }
            }
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (remaining in COUNTDOWN_SECONDS downTo 0) {
                _uiState.update { it.copy(remainingSeconds = remaining) }
                delay(1000)
            }
        }
    }

    companion object {
        const val CODE_LENGTH = 4
        private const val COUNTDOWN_SECONDS = 59
    }

    fun onCodeFocus() {
        _uiState.update {
            it.copy(isCodeError = false)
        }
    }
}