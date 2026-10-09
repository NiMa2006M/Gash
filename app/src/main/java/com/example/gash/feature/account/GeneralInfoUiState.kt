package com.example.gash.feature.account

import com.example.gash.ui.helper.UiText

data class GeneralInfoUiState(
    val farmName: String = "",
    val farmId: String = "",
    val phoneNumber: String = "",
    val farmNameError: Boolean = false,
    val farmIdError: Boolean = false,
    val phoneNumberError: Boolean = false,
    val profileImagePath: String? = null,
    val isImageProcessing: Boolean = false,
    val error: UiText? = null,
    val isSubmitting: Boolean = false,
    val isSaved: Boolean = false
)