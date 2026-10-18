package com.example.gash.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gash.domain.model.VaccineType
import com.example.gash.domain.usecase.vaccine.AddVaccineTypeUseCase
import com.example.gash.domain.usecase.vaccine.ArchiveVaccineTypeUseCase
import com.example.gash.domain.usecase.vaccine.ObserveVaccineTypesUseCase
import com.example.gash.domain.usecase.vaccine.RenameVaccineTypeUseCase
import com.example.gash.ui.helper.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaccineTypesViewModel @Inject constructor(
    observeVaccineTypesUseCase: ObserveVaccineTypesUseCase,
    private val addVaccineTypeUseCase: AddVaccineTypeUseCase,
    private val renameVaccineTypeUseCase: RenameVaccineTypeUseCase,
    private val archiveVaccineTypeUseCase: ArchiveVaccineTypeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VaccineTypesUiState())
    val uiState: StateFlow<VaccineTypesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeVaccineTypesUseCase().collect { types ->
                _uiState.update { it.copy(types = types) }
            }
        }
    }

    fun onAddClick() {
        _uiState.update { it.copy(dialogMode = VaccineTypeDialogMode.Add, dialogText = "", dialogError = null) }
    }

    fun onRenameClick(type: VaccineType) {
        _uiState.update {
            it.copy(dialogMode = VaccineTypeDialogMode.Rename(type), dialogText = type.name, dialogError = null)
        }
    }

    fun onDialogTextChange(value: String) {
        _uiState.update { it.copy(dialogText = value, dialogError = null) }
    }

    fun onDialogDismiss() {
        _uiState.update { it.copy(dialogMode = null, dialogText = "", dialogError = null) }
    }

    fun onDialogConfirm() {
        val state = _uiState.value
        viewModelScope.launch {
            val result = when (val mode = state.dialogMode) {
                is VaccineTypeDialogMode.Add -> addVaccineTypeUseCase(state.dialogText).map { }
                is VaccineTypeDialogMode.Rename -> renameVaccineTypeUseCase(mode.type.id, state.dialogText)
                null -> return@launch
            }
            if (result.isSuccess) {
                _uiState.update { it.copy(dialogMode = null, dialogText = "", dialogError = null) }
            } else {
                _uiState.update { it.copy(dialogError = result.exceptionOrNull()?.toUiText()) }
            }
        }
    }

    fun onArchiveClick(type: VaccineType) {
        _uiState.update { it.copy(typePendingArchive = type) }
    }

    fun onArchiveDismiss() {
        _uiState.update { it.copy(typePendingArchive = null) }
    }

    fun onArchiveConfirm() {
        val type = _uiState.value.typePendingArchive ?: return
        viewModelScope.launch {
            archiveVaccineTypeUseCase(type.id)
            _uiState.update { it.copy(typePendingArchive = null) }
        }
    }
}