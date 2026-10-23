package com.example.gash.feature.filetransfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gash.domain.usecase.export.CreateExportFileUseCase
import com.example.gash.domain.usecase.network.CheckInternetUseCase
import com.example.gash.domain.usecase.network.ObserveInternetStatusUseCase
import com.example.gash.domain.usecase.vaccination.ObserveVaccinationOperationSummariesUseCase
import com.example.gash.ui.helper.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class FileTransferViewModel @Inject constructor(
    observeInternetStatusUseCase: ObserveInternetStatusUseCase,
    private val checkInternetUseCase: CheckInternetUseCase,
    observeVaccinationOperationSummariesUseCase: ObserveVaccinationOperationSummariesUseCase,
    private val createExportFileUseCase: CreateExportFileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FileTransferUiState())
    val uiState: StateFlow<FileTransferUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<FileTransferEvent>()
    val events: SharedFlow<FileTransferEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            observeInternetStatusUseCase().collect { isOnline ->
                _uiState.update { it.copy(connectivity = isOnline.toStatus()) }
            }
        }
        viewModelScope.launch {
            observeVaccinationOperationSummariesUseCase().collect { operations ->
                _uiState.update { state ->
                    state.copy(
                        operations = operations,
                        selectedOperationId = state.selectedOperationId
                            ?.takeIf { id -> operations.any { it.id == id } }
                    )
                }
            }
        }
    }

    fun onRetryConnectivity() {
        viewModelScope.launch {
            _uiState.update { it.copy(connectivity = ConnectivityStatus.CHECKING) }
            delay(RETRY_FEEDBACK_DELAY_MILLIS.milliseconds)
            _uiState.update { it.copy(connectivity = checkInternetUseCase().toStatus()) }
        }
    }

    fun onOptionSelected(option: ExportOption) {
        _uiState.update { it.copy(selectedOption = option, error = null) }
    }

    fun onOperationSelected(operationId: Long) {
        _uiState.update { it.copy(selectedOperationId = operationId, error = null) }
    }

    fun onExportClick() {
        val state = _uiState.value
        val request = state.request ?: return
        if (state.isExporting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true, error = null) }
            createExportFileUseCase(request)
                .onSuccess { file ->
                    _uiState.update { it.copy(isExporting = false) }
                    _events.emit(FileTransferEvent.ShareFile(file))
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isExporting = false, error = error.toUiText()) }
                }
        }
    }

    private fun Boolean.toStatus() =
        if (this) ConnectivityStatus.ONLINE else ConnectivityStatus.OFFLINE

    private companion object {
        const val RETRY_FEEDBACK_DELAY_MILLIS = 400L
    }
}