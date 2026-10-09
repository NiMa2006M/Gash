package com.example.gash.feature.herd

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.gash.core.navigation.GashRoute
import com.example.gash.domain.model.Animal
import com.example.gash.domain.usecase.animal.GetAnimalsUseCase
import com.example.gash.domain.usecase.animal.GetUnassignedAnimalsUseCase
import com.example.gash.domain.usecase.animal.MoveAnimalToHerdUseCase
import com.example.gash.domain.usecase.animal.RegisterAnimalUseCase
import com.example.gash.domain.usecase.herd.ObserveHerdUseCase
import com.example.gash.ui.helper.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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

    private val searchQuery = MutableStateFlow("")

    private var unassignedJob: Job? = null

    init {
        viewModelScope.launch {
            observeHerdUseCase(herdId).collect { herd ->
                _uiState.update { it.copy(herd = herd) }
            }
        }

        viewModelScope.launch {
            combine(getAnimalsUseCase(herdId), searchQuery) { animals, query ->
                animals to query
            }.collect { (animals, query) ->
                _uiState.update {
                    it.copy(
                        animals = animals,
                        searchQuery = query,
                        filteredAnimals = filterAnimals(animals, query)
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    private fun filterAnimals(animals: List<Animal>, query: String): List<Animal> {
        val normalized = query.trim()
        if (normalized.isEmpty()) return animals
        return animals.filter { animal ->
            animal.id.toString().contains(normalized) ||
                    animal.activeRfidCode?.contains(normalized, ignoreCase = true) == true ||
                    animal.tag1?.contains(normalized, ignoreCase = true) == true ||
                    animal.tag2?.contains(normalized, ignoreCase = true) == true ||
                    animal.tag3?.contains(normalized, ignoreCase = true) == true
        }
    }

    fun onAddClick() {
        _uiState.update {
            it.copy(
                addSheetMode = AddAnimalMode.ChooseMethod,
                error = null
            )
        }
    }
    fun onDismissAddSheet() {
        unassignedJob?.cancel()
        unassignedJob = null
        _uiState.update {
            it.copy(
                addSheetMode = null,
                selectedUnassignedIds = emptySet(),
                unassignedAnimals = emptyList(),
                error = null
            )
        }
    }

    fun onPickExistingSelected() {
        _uiState.update {
            it.copy(
                addSheetMode = AddAnimalMode.PickExisting,
                error = null
            )
        }

        unassignedJob?.cancel()
        unassignedJob = viewModelScope.launch {
            getUnassignedAnimalsUseCase().collect { animals ->
                _uiState.update { it.copy(unassignedAnimals = animals) }
            }
        }
    }

    fun onRegisterNewSelected() {
        _uiState.update {
            it.copy(
                addSheetMode = AddAnimalMode.RegisterNew,
                error = null
            )
        }
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
            unassignedJob?.cancel()
            unassignedJob = null
            _uiState.update {
                it.copy(
                    isSubmitting = false,
                    addSheetMode = null,
                    selectedUnassignedIds = emptySet(),
                    unassignedAnimals = emptyList()
                )
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

    fun onRemoveAnimalFromHerd(animalId: Long) {
        viewModelScope.launch {
            moveAnimalToHerdUseCase(
                animalId = animalId,
                newHerdId = null
            )
        }
    }

    fun onRfidCodeChange() {
        if (_uiState.value.error != null) {
            _uiState.update {
                it.copy(error = null)
            }
        }
    }
}