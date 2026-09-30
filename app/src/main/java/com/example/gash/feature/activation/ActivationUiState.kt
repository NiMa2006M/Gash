package com.example.gash.feature.activation

import com.example.gash.ui.helper.UiText

enum class ActivationStep { Form, CodeEntry }

data class ActivationUiState(
    val farmName: String = "",
    val farmId: String = "",
    val phoneNumber: String = "",
    val formError: UiText? = null,
    val step: ActivationStep = ActivationStep.Form,
    val enteredCode: String = "",
    val isCodeError: Boolean = false,
    val remainingSeconds: Int = 59,
    val isSubmitting: Boolean = false
)

