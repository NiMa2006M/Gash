package com.example.gash.feature.herd

import com.example.gash.domain.model.Herd
import com.example.gash.ui.helper.UiText

data class HerdManagementUiState(
    val herds: List<Herd> = emptyList(),
    val dialogMode: HerdDialogMode? = null,
    val dialogText: String = "",
    val dialogError: UiText? = null,
    val herdPendingDelete: Herd? = null
)

sealed interface HerdDialogMode {
    data object Add : HerdDialogMode
    data class Rename(val herd: Herd) : HerdDialogMode
}