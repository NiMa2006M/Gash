package com.example.gash.feature.filetransfer

import com.example.gash.domain.model.ExportFile
import com.example.gash.domain.model.ExportRequest
import com.example.gash.domain.model.VaccinationOperationSummary
import com.example.gash.ui.helper.UiText

enum class ConnectivityStatus { CHECKING, ONLINE, OFFLINE }

sealed interface FileTransferEvent {
    data class ShareFile(val file: ExportFile) : FileTransferEvent
}

data class FileTransferUiState(
    val connectivity: ConnectivityStatus = ConnectivityStatus.CHECKING,
    val selectedOption: ExportOption? = null,
    val operations: List<VaccinationOperationSummary> = emptyList(),
    val selectedOperationId: Long? = null,
    val isExporting: Boolean = false,
    val error: UiText? = null
) {
    val request: ExportRequest?
        get() = when (selectedOption) {
            ExportOption.ANIMALS_WEIGHTS -> ExportRequest.AnimalsWeights
            ExportOption.VACCINATION ->
                selectedOperationId?.let { ExportRequest.VaccinationOperation(it) }
            null -> null
        }

    val canExport: Boolean get() = request != null
}