package com.example.gash.feature.animal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.gash.core.navigation.GashRoute
import com.example.gash.domain.usecase.animal.ExpireAnimalUseCase
import com.example.gash.domain.usecase.animal.MoveAnimalToHerdUseCase
import com.example.gash.domain.usecase.animal.ObserveAnimalUseCase
import com.example.gash.domain.usecase.animal.RestoreAnimalUseCase
import com.example.gash.domain.usecase.herd.GetHerdsUseCase
import com.example.gash.domain.usecase.herd.ObserveHerdUseCase
import com.example.gash.domain.usecase.weight.GetWeightHistoryUseCase
import com.example.gash.domain.usecase.weight.RecordWeightUseCase
import com.example.gash.ui.helper.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimalDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeAnimalUseCase: ObserveAnimalUseCase,
    private val observeHerdUseCase: ObserveHerdUseCase,
    private val getHerdsUseCase: GetHerdsUseCase,
    private val getWeightHistoryUseCase: GetWeightHistoryUseCase,
    private val recordWeightUseCase: RecordWeightUseCase,
    private val moveAnimalToHerdUseCase: MoveAnimalToHerdUseCase,
    private val expireAnimalUseCase: ExpireAnimalUseCase,
    private val restoreAnimalUseCase: RestoreAnimalUseCase
) : ViewModel() {

    private val animalId: Long = savedStateHandle.toRoute<GashRoute.AnimalDetail>().animalId

    private val _uiState = MutableStateFlow(AnimalDetailUiState())
    val uiState: StateFlow<AnimalDetailUiState> = _uiState.asStateFlow()

    private var herdJob: Job? = null

    init {
        viewModelScope.launch {
            observeAnimalUseCase(animalId).collect { animal ->
                _uiState.update { it.copy(animal = animal) }
                observeHerdInfo(animal?.herdId)
            }
        }
        viewModelScope.launch {
            getWeightHistoryUseCase(animalId).collect { history ->
                _uiState.update { it.copy(weightHistory = history) }
            }
        }
    }

    private fun observeHerdInfo(herdId: Long?) {
        herdJob?.cancel()
        if (herdId == null) {
            _uiState.update { it.copy(herd = null) }
            return
        }
        herdJob = viewModelScope.launch {
            observeHerdUseCase(herdId).collect { herd ->
                _uiState.update { it.copy(herd = herd) }
            }
        }
    }

    fun onRecordWeightClick() {
        _uiState.update { it.copy(isRecordWeightSheetOpen = true, error = null) }
    }

    fun onDismissRecordWeight() {
        _uiState.update { it.copy(isRecordWeightSheetOpen = false, error = null) }
    }

    fun onConfirmRecordWeight(weightKg: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            val result = recordWeightUseCase(animalId, weightKg)
            _uiState.update {
                if (result.isSuccess) it.copy(isSubmitting = false, isRecordWeightSheetOpen = false)
                else it.copy(isSubmitting = false, error = result.exceptionOrNull()?.toUiText())
            }
        }
    }

    fun onMoveHerdClick() {
        _uiState.update { it.copy(isMoveHerdSheetOpen = true) }
        viewModelScope.launch {
            getHerdsUseCase().collect { herds ->
                _uiState.update { it.copy(availableHerds = herds) }
            }
        }
    }

    fun onDismissMoveHerd() {
        _uiState.update { it.copy(isMoveHerdSheetOpen = false) }
    }

    fun onHerdSelected(herdId: Long) {
        viewModelScope.launch {
            moveAnimalToHerdUseCase(animalId, herdId)
            _uiState.update { it.copy(isMoveHerdSheetOpen = false) }
        }
    }

    fun onExpireClick() {
        _uiState.update { it.copy(isExpireConfirmOpen = true) }
    }

    fun onDismissExpireConfirm() {
        _uiState.update { it.copy(isExpireConfirmOpen = false) }
    }

    fun onConfirmExpire() {
        viewModelScope.launch {
            expireAnimalUseCase(animalId)
            _uiState.update { it.copy(isExpireConfirmOpen = false) }
        }
    }

    fun onRestoreClick() {
        viewModelScope.launch { restoreAnimalUseCase(animalId) }
    }
}