package com.example.gash.feature.vaccination

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.core.operation.OperationBottomButton
import com.example.gash.ui.theme.GashOrange
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun VaccinationSettingsTab(
    uiState: VaccinationUiState,
    actions: VaccinationActions,
    modifier: Modifier = Modifier
) {
    val locked = uiState.isSettingsLocked
    val incomplete = !locked && uiState.currentSettings == null

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (locked) LockedBanner()

            SettingsSection(title = stringResource(R.string.vaccination_section_required)) {
                SettingsPickerField(
                    label = stringResource(R.string.vaccination_field_vaccine_type),
                    required = true,
                    value = uiState.vaccineType?.name,
                    placeholder = stringResource(R.string.vaccination_field_vaccine_type_hint),
                    enabled = !locked,
                    onClick = { actions.onOpenPicker(SettingsPicker.VACCINE_TYPE) }
                )
                InjectionSiteSelector(
                    selected = uiState.injectionSite,
                    enabled = !locked,
                    onSelected = actions.onInjectionSiteSelected
                )
            }

            SettingsSection(title = stringResource(R.string.vaccination_section_optional)) {
                SettingsTextField(
                    label = stringResource(R.string.vaccination_field_dose),
                    value = uiState.doseText,
                    onValueChange = actions.onDoseChange,
                    placeholder = stringResource(R.string.vaccination_dose_hint),
                    enabled = !locked,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    suffix = stringResource(R.string.vaccination_dose_unit),
                    errorText = if (!uiState.isDoseValid) {
                        stringResource(R.string.vaccination_dose_invalid)
                    } else null
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SettingsTextField(
                        label = stringResource(R.string.vaccination_field_operator),
                        value = uiState.operatorName,
                        onValueChange = actions.onOperatorNameChange,
                        placeholder = stringResource(R.string.vaccination_operator_hint),
                        enabled = !locked,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        )
                    )
                    if (!locked && uiState.recentOperatorNames.isNotEmpty()) {
                        OperatorSuggestions(
                            names = uiState.recentOperatorNames,
                            current = uiState.operatorName,
                            onSelect = actions.onOperatorNameChange
                        )
                    }
                }

                SettingsPickerField(
                    label = stringResource(R.string.vaccination_field_herd),
                    required = false,
                    value = uiState.herdName,
                    placeholder = stringResource(R.string.vaccination_herd_none),
                    enabled = !locked,
                    onClick = { actions.onOpenPicker(SettingsPicker.HERD) }
                )
            }
        }

        Column {
            if (incomplete) {
                Text(
                    text = stringResource(R.string.vaccination_settings_incomplete_hint),
                    color = GashTextSecondary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }
            OperationBottomButton(
                text = stringResource(
                    if (locked) R.string.vaccination_btn_back_to_register else R.string.vaccination_btn_continue
                ),
                enabled = locked || uiState.currentSettings != null,
                onClick = actions.onContinueClick
            )
        }
    }
}

@Composable
private fun LockedBanner() {
    Text(
        text = stringResource(R.string.vaccination_settings_locked),
        color = GashOrange,
        fontSize = 13.sp,
        modifier = Modifier
            .fillMaxWidth()
            .background(GashOrange.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    )
}