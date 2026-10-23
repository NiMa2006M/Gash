package com.example.gash.feature.filetransfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.domain.model.VaccinationOperationSummary
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashTextSecondary
import java.util.Locale

@Composable
fun FileTransferScreen(
    uiState: FileTransferUiState,
    onRetryConnectivity: () -> Unit,
    onOptionSelected: (ExportOption) -> Unit,
    onOperationSelected: (Long) -> Unit,
    onExportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        FileTransferHeader(
            title = stringResource(R.string.file_transfer_title),
            subtitle = stringResource(R.string.file_transfer_subtitle)
        )

        when (uiState.connectivity) {
            ConnectivityStatus.CHECKING -> Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GashGreen)
            }

            ConnectivityStatus.OFFLINE -> NoInternetContent(
                onRetry = onRetryConnectivity,
                modifier = Modifier.weight(1f)
            )

            ConnectivityStatus.ONLINE -> ExportContent(
                uiState = uiState,
                onOptionSelected = onOptionSelected,
                onOperationSelected = onOperationSelected,
                onExportClick = onExportClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ExportContent(
    uiState: FileTransferUiState,
    onOptionSelected: (ExportOption) -> Unit,
    onOperationSelected: (Long) -> Unit,
    onExportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val locale = LocalConfiguration.current.locales[0]

    Column(modifier = modifier.fillMaxWidth()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item(key = "title") {
                Text(
                    text = stringResource(R.string.file_transfer_choose_data),
                    color = GashTextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            ExportOption.entries.forEach { option ->
                item(key = option.name) {
                    ExportOptionCard(
                        option = option,
                        selected = option == uiState.selectedOption,
                        onClick = { onOptionSelected(option) }
                    )
                }

                if (option == ExportOption.VACCINATION && uiState.selectedOption == option) {
                    vaccinationOperationItems(
                        operations = uiState.operations,
                        selectedId = uiState.selectedOperationId,
                        locale = locale,
                        onSelect = onOperationSelected
                    )
                }
            }
        }

        ExportActionBar(
            canExport = uiState.canExport,
            isExporting = uiState.isExporting,
            error = uiState.error,
            onClick = onExportClick
        )
    }
}

private fun LazyListScope.vaccinationOperationItems(
    operations: List<VaccinationOperationSummary>,
    selectedId: Long?,
    locale: Locale,
    onSelect: (Long) -> Unit
) {
    item(key = "operations_title") {
        Text(
            text = stringResource(R.string.file_transfer_choose_operation),
            color = GashTextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 12.dp, top = 4.dp)
        )
    }

    if (operations.isEmpty()) {
        item(key = "operations_empty") {
            Text(
                text = stringResource(R.string.file_transfer_operations_empty),
                color = GashTextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
            )
        }
    } else {
        items(items = operations, key = { "operation_${it.id}" }) { operation ->
            VaccinationOperationItem(
                summary = operation,
                selected = operation.id == selectedId,
                locale = locale,
                onClick = { onSelect(operation.id) },
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}