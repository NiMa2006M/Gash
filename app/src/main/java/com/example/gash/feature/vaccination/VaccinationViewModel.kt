package com.example.gash.feature.vaccination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gash.domain.error.DomainError
import com.example.gash.domain.model.InjectionSite
import com.example.gash.domain.model.VaccinationOperation
import com.example.gash.domain.model.VaccinationRecord
import com.example.gash.domain.model.VaccinationSettings
import com.example.gash.domain.model.VaccineType
import com.example.gash.domain.usecase.herd.GetHerdsUseCase
import com.example.gash.domain.usecase.herd.ObserveHerdUseCase
import com.example.gash.domain.usecase.vaccination.FinishVaccinationOperationUseCase
import com.example.gash.domain.usecase.vaccination.GetOpenVaccinationOperationUseCase
import com.example.gash.domain.usecase.vaccination.ObserveRecentOperatorNamesUseCase
import com.example.gash.domain.usecase.vaccination.ObserveVaccinationRecordsUseCase
import com.example.gash.domain.usecase.vaccination.RegisterVaccinationUseCase
import com.example.gash.domain.usecase.vaccination.RemoveVaccinationRecordUseCase
import com.example.gash.domain.usecase.vaccine.ObserveVaccineTypesUseCase
import com.example.gash.ui.helper.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
class VaccinationViewModel @Inject constructor(
    observeVaccineTypesUseCase: ObserveVaccineTypesUseCase,
    observeRecentOperatorNamesUseCase: ObserveRecentOperatorNamesUseCase,
    getHerdsUseCase: GetHerdsUseCase,
    private val observeHerdUseCase: ObserveHerdUseCase,
    private val getOpenVaccinationOperationUseCase: GetOpenVaccinationOperationUseCase,
    private val observeVaccinationRecordsUseCase: ObserveVaccinationRecordsUseCase,
    private val registerVaccinationUseCase: RegisterVaccinationUseCase,
    private val removeVaccinationRecordUseCase: RemoveVaccinationRecordUseCase,
    private val finishVaccinationOperationUseCase: FinishVaccinationOperationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VaccinationUiState())
    val uiState: StateFlow<VaccinationUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<VaccinationEvent>()
    val events: SharedFlow<VaccinationEvent> = _events.asSharedFlow()

    private var recordsJob: Job? = null
    private var herdCountJob: Job? = null

    init {
        viewModelScope.launch {
            observeVaccineTypesUseCase().collect { types ->
                _uiState.update { it.copy(availableVaccineTypes = types) }
            }
        }
        viewModelScope.launch {
            getHerdsUseCase().collect { herds ->
                _uiState.update { it.copy(availableHerds = herds) }
            }
        }
        viewModelScope.launch {
            val open = getOpenVaccinationOperationUseCase()
            if (open != null) applyOperation(open, switchToRegister = true)
            _uiState.update { it.copy(isLoading = false) }
        }
        viewModelScope.launch {
            observeRecentOperatorNamesUseCase().collect { names ->
                _uiState.update { it.copy(recentOperatorNames = names) }
            }
        }
    }

    fun onTabSelected(tab: VaccinationTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun onContinueClick() {
        val state = _uiState.value
        if (state.operation != null || state.currentSettings != null) {
            _uiState.update { it.copy(selectedTab = VaccinationTab.REGISTER) }
        }
    }

    fun onOpenPicker(picker: SettingsPicker) {
        if (_uiState.value.isSettingsLocked) return
        _uiState.update { it.copy(picker = picker) }
    }

    fun onDismissPicker() {
        _uiState.update { it.copy(picker = null) }
    }

    fun onVaccineTypeSelected(type: VaccineType) {
        if (_uiState.value.isSettingsLocked) return
        _uiState.update { it.copy(vaccineType = type, picker = null) }
    }

    fun onInjectionSiteSelected(site: InjectionSite) {
        if (_uiState.value.isSettingsLocked) return
        _uiState.update { it.copy(injectionSite = site) }
    }

    fun onDoseChange(text: String) {
        if (_uiState.value.isSettingsLocked) return
        _uiState.update { it.copy(doseText = text.take(MAX_DOSE_LENGTH)) }
    }

    fun onOperatorNameChange(text: String) {
        if (_uiState.value.isSettingsLocked) return
        _uiState.update { it.copy(operatorName = text.take(VaccinationSettings.MAX_OPERATOR_NAME_LENGTH)) }
    }

    fun onHerdSelected(herdId: Long?, herdName: String?) {
        if (_uiState.value.isSettingsLocked) return
        _uiState.update { it.copy(herdId = herdId, herdName = herdName, picker = null) }
        observeHerdCount(herdId)
    }


    fun onRfidChange(text: String) {
        _uiState.update { it.copy(rfidText = text, feedback = null) }
    }

    fun onRegisterClick() {
        register(rfidCode = _uiState.value.rfidText, confirmHerdMismatch = false)
    }

    fun onConfirmHerdMismatch() {
        val prompt = _uiState.value.herdMismatchPrompt ?: return
        _uiState.update { it.copy(herdMismatchPrompt = null) }
        register(rfidCode = prompt.rfidCode, confirmHerdMismatch = true)
    }

    fun onDismissHerdMismatch() {
        _uiState.update { it.copy(herdMismatchPrompt = null) }
    }

    private fun register(rfidCode: String, confirmHerdMismatch: Boolean) {
        val state = _uiState.value
        val settings = state.currentSettings ?: return
        if (state.isSubmitting || rfidCode.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, feedback = null) }

            val result = registerVaccinationUseCase(
                operationId = state.operation?.id,
                settings = settings,
                rfidCode = rfidCode,
                allowHerdMismatch = confirmHerdMismatch
            )

            result
                .onSuccess { registration ->
                    if (state.operation == null) {
                        applyOperation(registration.operation, switchToRegister = false)
                    }
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            rfidText = "",
                            feedback = RegisterFeedback.Success(rfidCode.trim())
                        )
                    }
                }
                .onFailure { error ->
                    if (error is DomainError.AnimalNotInSelectedHerd) {
                        _uiState.update {
                            it.copy(
                                isSubmitting = false,
                                herdMismatchPrompt = HerdMismatchPrompt(rfidCode.trim(), error.animalId)
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(isSubmitting = false, feedback = RegisterFeedback.Failure(error.toUiText()))
                        }
                    }
                }
        }
    }


    fun onRecordDeleteClick(record: VaccinationRecord) {
        _uiState.update { it.copy(recordPendingDelete = record) }
    }

    fun onDismissDeleteRecord() {
        _uiState.update { it.copy(recordPendingDelete = null) }
    }

    fun onConfirmDeleteRecord() {
        val record = _uiState.value.recordPendingDelete ?: return
        viewModelScope.launch {
            removeVaccinationRecordUseCase(record.id)
                .onSuccess { operationDeleted ->
                    _uiState.update { it.copy(recordPendingDelete = null) }
                    if (operationDeleted) resetToDraft()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            recordPendingDelete = null,
                            feedback = RegisterFeedback.Failure(error.toUiText())
                        )
                    }
                }
        }
    }


    fun onFinishClick() {
        if (_uiState.value.operation == null) {
            viewModelScope.launch { _events.emit(VaccinationEvent.Finished) }
        } else {
            _uiState.update { it.copy(isFinishConfirmOpen = true) }
        }
    }

    fun onDismissFinish() {
        _uiState.update { it.copy(isFinishConfirmOpen = false) }
    }

    fun onConfirmFinish() {
        val operation = _uiState.value.operation ?: return
        viewModelScope.launch {
            finishVaccinationOperationUseCase(operation.id)
                .onSuccess { _events.emit(VaccinationEvent.Finished) }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isFinishConfirmOpen = false,
                            feedback = RegisterFeedback.Failure(error.toUiText())
                        )
                    }
                }
        }
    }


    private fun applyOperation(operation: VaccinationOperation, switchToRegister: Boolean) {
        _uiState.update {
            it.copy(
                operation = operation,
                vaccineType = VaccineType(operation.vaccineTypeId, operation.vaccineTypeName),
                injectionSite = operation.injectionSite,
                doseText = operation.doseMl?.let(::formatDose).orEmpty(),
                operatorName = operation.operatorName.orEmpty(),
                herdId = operation.herdId,
                herdName = operation.herdName,
                selectedTab = if (switchToRegister) VaccinationTab.REGISTER else it.selectedTab
            )
        }
        observeRecords(operation.id)
        observeHerdCount(operation.herdId)
    }

    private fun resetToDraft() {
        recordsJob?.cancel()
        _uiState.update { it.copy(operation = null, records = emptyList()) }
    }

    private fun observeRecords(operationId: Long) {
        recordsJob?.cancel()
        recordsJob = viewModelScope.launch {
            observeVaccinationRecordsUseCase(operationId).collect { records ->
                _uiState.update { it.copy(records = records) }
            }
        }
    }

    private fun observeHerdCount(herdId: Long?) {
        herdCountJob?.cancel()
        if (herdId == null) {
            _uiState.update { it.copy(herdAnimalCount = null) }
            return
        }
        herdCountJob = viewModelScope.launch {
            observeHerdUseCase(herdId).collect { herd ->
                _uiState.update { it.copy(herdAnimalCount = herd?.animalCount) }
            }
        }
    }

    private companion object {
        const val MAX_DOSE_LENGTH = 8
    }
}