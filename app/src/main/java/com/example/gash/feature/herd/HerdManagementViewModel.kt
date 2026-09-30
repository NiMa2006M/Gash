package com.example.gash.feature.herd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gash.domain.model.Herd
import com.example.gash.domain.usecase.herd.AddHerdUseCase
import com.example.gash.domain.usecase.herd.DeleteHerdUseCase
import com.example.gash.domain.usecase.herd.GetHerdsUseCase
import com.example.gash.domain.usecase.herd.RenameHerdUseCase
import com.example.gash.ui.helper.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HerdManagementViewModel @Inject constructor(
    private val getHerdsUseCase: GetHerdsUseCase,
    private val addHerdUseCase: AddHerdUseCase,
    private val renameHerdUseCase: RenameHerdUseCase,
    private val deleteHerdUseCase: DeleteHerdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HerdManagementUiState())
    val uiState: StateFlow<HerdManagementUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getHerdsUseCase().collect { herds ->
                _uiState.update { it.copy(herds = herds) }
            }
        }
    }

    fun onAddClick() {
        _uiState.update { it.copy(dialogMode = HerdDialogMode.Add, dialogText = "", dialogError = null) }
    }

    fun onRenameClick(herd: Herd) {
        _uiState.update {
            it.copy(dialogMode = HerdDialogMode.Rename(herd), dialogText = herd.name, dialogError = null)
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
        when (val mode = state.dialogMode) {
            is HerdDialogMode.Add -> viewModelScope.launch {
                handleDialogResult(addHerdUseCase(state.dialogText))
            }
            is HerdDialogMode.Rename -> viewModelScope.launch {
                handleDialogResult(renameHerdUseCase(mode.herd.id, state.dialogText))
            }
            null -> Unit
        }
    }

    private fun handleDialogResult(result: Result<*>) {
        if (result.isFailure) {
            _uiState.update { it.copy(dialogError = result.exceptionOrNull()?.toUiText()) }
        } else {
            _uiState.update { it.copy(dialogMode = null, dialogText = "", dialogError = null) }
        }
    }

    fun onDeleteClick(herd: Herd) {
        _uiState.update { it.copy(herdPendingDelete = herd) }
    }

    fun onDeleteDismiss() {
        _uiState.update { it.copy(herdPendingDelete = null) }
    }

    fun onDeleteConfirm() {
        val herd = _uiState.value.herdPendingDelete ?: return
        viewModelScope.launch {
            deleteHerdUseCase(herd.id)
            _uiState.update { it.copy(herdPendingDelete = null) }
        }
    }
}