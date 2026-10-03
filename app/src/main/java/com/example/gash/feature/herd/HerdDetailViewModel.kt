package com.example.gash.feature.herd

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.gash.core.navigation.GashRoute
import com.example.gash.domain.usecase.animal.GetAnimalsUseCase
import com.example.gash.domain.usecase.animal.GetUnassignedAnimalsUseCase
import com.example.gash.domain.usecase.animal.MoveAnimalToHerdUseCase
import com.example.gash.domain.usecase.animal.RegisterAnimalUseCase
import com.example.gash.domain.usecase.herd.ObserveHerdUseCase
import com.example.gash.ui.helper.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HerdDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeHerdUseCase: ObserveHerdUseCase,
    getAnimalsUseCase: GetAnimalsUseCase,
    private val getUnassignedAnimalsUseCase: GetUnassignedAnimalsUseCase,
    private val moveAnimalToHerdUseCase: MoveAnimalToHerdUseCase,
    private val registerAnimalUseCase: RegisterAnimalUseCase
) : ViewModel() {

    private val herdId: Long = savedStateHandle.toRoute<GashRoute.HerdDetail>().herdId

    private val _uiState = MutableStateFlow(HerdDetailUiState())
    val uiState: StateFlow<HerdDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeHerdUseCase(herdId).collect { herd ->
                _uiState.update { it.copy(herd = herd) }
            }
        }
        viewModelScope.launch {
            getAnimalsUseCase(herdId).collect { animals ->
                _uiState.update { it.copy(animals = animals) }
            }
        }
    }

    fun onAddClick() {
        _uiState.update { it.copy(addSheetMode = AddAnimalMode.ChooseMethod) }
    }

    fun onDismissAddSheet() {
        _uiState.update {
            it.copy(addSheetMode = null, selectedUnassignedIds = emptySet(), error = null)
        }
    }

    fun onPickExistingSelected() {
        _uiState.update { it.copy(addSheetMode = AddAnimalMode.PickExisting) }
        viewModelScope.launch {
            getUnassignedAnimalsUseCase().collect { animals ->
                _uiState.update { it.copy(unassignedAnimals = animals) }
            }
        }
    }

    fun onRegisterNewSelected() {
        _uiState.update { it.copy(addSheetMode = AddAnimalMode.RegisterNew) }
    }

    fun onToggleUnassignedSelection(animalId: Long) {
        _uiState.update { state ->
            val newSelection = if (animalId in state.selectedUnassignedIds) {
                state.selectedUnassignedIds - animalId
            } else {
                state.selectedUnassignedIds + animalId
            }
            state.copy(selectedUnassignedIds = newSelection)
        }
    }

    fun onConfirmAddExisting() {
        val ids = _uiState.value.selectedUnassignedIds
        if (ids.isEmpty()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            ids.forEach { animalId -> moveAnimalToHerdUseCase(animalId, herdId) }
            _uiState.update {
                it.copy(isSubmitting = false, addSheetMode = null, selectedUnassignedIds = emptySet())
            }
        }
    }

    fun onConfirmRegisterNew(rfidCode: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            val result = registerAnimalUseCase(herdId, rfidCode?.takeIf { it.isNotBlank() })
            _uiState.update {
                if (result.isSuccess) it.copy(isSubmitting = false, addSheetMode = null)
                else it.copy(isSubmitting = false, error = result.exceptionOrNull()?.toUiText())
            }
        }
    }
}