package com.example.gash.feature.vaccination

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gash.R
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinationSheetsAndDialogs(
    uiState: VaccinationUiState,
    actions: VaccinationActions
) {
    when (uiState.picker) {
        SettingsPicker.VACCINE_TYPE -> ModalBottomSheet(onDismissRequest = actions.onDismissPicker) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.vaccination_picker_vaccine_title),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
                if (uiState.availableVaccineTypes.isEmpty()) {
                    Text(
                        text = stringResource(R.string.vaccination_picker_vaccine_empty),
                        color = GashTextSecondary,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                    OutlinedButton(
                        onClick = {
                            actions.onDismissPicker()
                            actions.onGoToVaccineSettings()
                        },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(stringResource(R.string.vaccination_define_vaccines), color = GashGreen)
                    }
                } else {
                    uiState.availableVaccineTypes.forEach { type ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = type.name,
                                    fontWeight = if (type.id == uiState.vaccineType?.id) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            modifier = Modifier.clickable { actions.onVaccineTypeSelected(type) }
                        )
                    }
                }
            }
        }

        SettingsPicker.HERD -> ModalBottomSheet(onDismissRequest = actions.onDismissPicker) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.vaccination_picker_herd_title),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
                ListItem(
                    headlineContent = { Text(stringResource(R.string.vaccination_herd_none)) },
                    modifier = Modifier.clickable { actions.onHerdSelected(null, null) }
                )
                uiState.availableHerds.forEach { herd ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = herd.name,
                                fontWeight = if (herd.id == uiState.herdId) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.clickable { actions.onHerdSelected(herd.id, herd.name) }
                    )
                }
            }
        }

        null -> Unit
    }

    uiState.herdMismatchPrompt?.let { prompt ->
        AlertDialog(
            onDismissRequest = actions.onDismissHerdMismatch,
            title = { Text(stringResource(R.string.vaccination_mismatch_title)) },
            text = {
                Text(
                    stringResource(
                        R.string.vaccination_mismatch_message,
                        prompt.rfidCode,
                        uiState.herdName.orEmpty()
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = actions.onConfirmHerdMismatch) {
                    Text(stringResource(R.string.common_btn_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = actions.onDismissHerdMismatch) {
                    Text(stringResource(R.string.common_btn_cancel))
                }
            }
        )
    }

    uiState.recordPendingDelete?.let { record ->
        AlertDialog(
            onDismissRequest = actions.onDismissDeleteRecord,
            title = { Text(stringResource(R.string.vaccination_delete_title)) },
            text = {
                Text(
                    stringResource(
                        R.string.vaccination_delete_message,
                        record.scannedRfidCode ?: stringResource(R.string.vaccination_rfid_unknown)
                    )
                )
            },            confirmButton = {
                TextButton(onClick = actions.onConfirmDeleteRecord) {
                    Text(stringResource(R.string.common_btn_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = actions.onDismissDeleteRecord) {
                    Text(stringResource(R.string.common_btn_cancel))
                }
            }
        )
    }

    val operation = uiState.operation
    if (uiState.isFinishConfirmOpen && operation != null) {
        AlertDialog(
            onDismissRequest = actions.onDismissFinish,
            title = { Text(stringResource(R.string.vaccination_finish_title)) },
            text = {
                Text(
                    stringResource(
                        R.string.vaccination_finish_message,
                        operation.id,
                        uiState.records.size
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = actions.onConfirmFinish) {
                    Text(stringResource(R.string.vaccination_btn_finish))
                }
            },
            dismissButton = {
                TextButton(onClick = actions.onDismissFinish) {
                    Text(stringResource(R.string.common_btn_cancel))
                }
            }
        )
    }
}