package com.example.gash.feature.vaccination

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.core.operation.OperationBottomButton
import com.example.gash.ui.theme.GashError_RED
import com.example.gash.ui.theme.GashFieldBackground
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashTextPrimary
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun VaccinationRegisterTab(
    uiState: VaccinationUiState,
    actions: VaccinationActions,
    modifier: Modifier = Modifier
) {
    if (!uiState.canRegister) {
        SettingsRequiredCard(
            onGoToSettings = { actions.onTabSelected(VaccinationTab.SETTINGS) },
            modifier = modifier
        )
    } else {
        RegisterContent(uiState, actions, modifier)
    }
}

@Composable
private fun RegisterContent(
    uiState: VaccinationUiState,
    actions: VaccinationActions,
    modifier: Modifier
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    LaunchedEffect(uiState.feedback) {
        if (uiState.feedback is RegisterFeedback.Success) focusRequester.requestFocus()
    }

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SettingsSummaryCard(uiState)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                OutlinedTextField(
                    value = uiState.rfidText,
                    onValueChange = actions.onRfidChange,
                    placeholder = { Text(stringResource(R.string.vaccination_rfid_hint)) },
                    isError = uiState.feedback is RegisterFeedback.Failure,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { actions.onRegisterClick() }),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f).focusRequester(focusRequester)
                )
                Button(
                    onClick = actions.onRegisterClick,
                    enabled = uiState.rfidText.isNotBlank() && !uiState.isSubmitting,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GashGreen, contentColor = Color.White),
                    modifier = Modifier.height(56.dp)
                ) {
                    Text(stringResource(R.string.vaccination_btn_register))
                }
            }

            when (val feedback = uiState.feedback) {
                is RegisterFeedback.Success -> Text(
                    text = stringResource(R.string.vaccination_registered_success, feedback.animalId),
                    color = GashGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                is RegisterFeedback.Failure -> Text(
                    text = feedback.message.asString(),
                    color = GashError_RED,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                null -> Unit
            }

            Text(
                text = if (uiState.herdAnimalCount != null) {
                    stringResource(
                        R.string.vaccination_count_of_herd,
                        uiState.records.size,
                        uiState.herdAnimalCount
                    )
                } else {
                    stringResource(R.string.vaccination_count, uiState.records.size)
                },
                color = GashTextSecondary,
                fontSize = 13.sp
            )
        }

        if (uiState.records.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.vaccination_records_empty),
                    color = GashTextSecondary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(items = uiState.records, key = { _, record -> record.id }) { index, record ->
                    VaccinationRecordCard(
                        number = uiState.records.size - index,
                        record = record,
                        onClick = { actions.onRecordClick(record) },
                        onDeleteClick = { actions.onRecordDeleteClick(record) }
                    )
                }
            }
        }

        OperationBottomButton(
            text = stringResource(R.string.vaccination_btn_finish),
            onClick = actions.onFinishClick
        )
    }
}

@Composable
private fun SettingsSummaryCard(uiState: VaccinationUiState) {
    val siteLabel = uiState.injectionSite?.let { stringResource(it.labelRes()) }
    val doseLabel = uiState.doseValue?.takeIf { uiState.doseText.isNotBlank() }
        ?.let { stringResource(R.string.vaccination_dose_value, formatDose(it)) }

    val details = listOfNotNull(siteLabel, doseLabel, uiState.herdName).joinToString(" · ")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GashFieldBackground, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = uiState.vaccineType?.name.orEmpty(),
            color = GashTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        if (details.isNotEmpty()) {
            Text(text = details, color = GashTextSecondary, fontSize = 12.sp)
        }
    }
}

@Composable
private fun SettingsRequiredCard(
    onGoToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.vaccination_settings_required_title),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = GashTextPrimary
            )
            Text(
                text = stringResource(R.string.vaccination_settings_required_body),
                color = GashTextSecondary,
                fontSize = 13.sp
            )
            Spacer(Modifier.height(4.dp))
            OutlinedButton(onClick = onGoToSettings) {
                Text(stringResource(R.string.vaccination_go_to_settings), color = GashGreen)
            }
        }
    }
}