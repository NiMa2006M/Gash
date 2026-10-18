package com.example.gash.feature.account

import com.example.gash.domain.model.VaccineType
import com.example.gash.ui.helper.UiText

data class VaccineTypesUiState(
    val types: List<VaccineType> = emptyList(),
    val dialogMode: VaccineTypeDialogMode? = null,
    val dialogText: String = "",
    val dialogError: UiText? = null,
    val typePendingArchive: VaccineType? = null
)

sealed interface VaccineTypeDialogMode {
    data object Add : VaccineTypeDialogMode
    data class Rename(val type: VaccineType) : VaccineTypeDialogMode
}