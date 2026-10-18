package com.example.gash.feature.vaccination

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun VaccinationRoute(
    onBack: () -> Unit,
    onAnimalClick: (Long) -> Unit,
    onNavigateToVaccineSettings: () -> Unit,
    viewModel: VaccinationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is VaccinationEvent.Finished) onBack()
        }
    }

    val actions = remember(viewModel, onBack, onAnimalClick, onNavigateToVaccineSettings) {
        VaccinationActions(
            onBack = onBack,
            onTabSelected = viewModel::onTabSelected,
            onOpenPicker = viewModel::onOpenPicker,
            onDismissPicker = viewModel::onDismissPicker,
            onVaccineTypeSelected = viewModel::onVaccineTypeSelected,
            onHerdSelected = viewModel::onHerdSelected,
            onInjectionSiteSelected = viewModel::onInjectionSiteSelected,
            onDoseChange = viewModel::onDoseChange,
            onOperatorNameChange = viewModel::onOperatorNameChange,
            onContinueClick = viewModel::onContinueClick,
            onGoToVaccineSettings = onNavigateToVaccineSettings,
            onRfidChange = viewModel::onRfidChange,
            onRegisterClick = viewModel::onRegisterClick,
            onConfirmHerdMismatch = viewModel::onConfirmHerdMismatch,
            onDismissHerdMismatch = viewModel::onDismissHerdMismatch,
            onRecordClick = { record -> onAnimalClick(record.animalId) },
            onRecordDeleteClick = viewModel::onRecordDeleteClick,
            onConfirmDeleteRecord = viewModel::onConfirmDeleteRecord,
            onDismissDeleteRecord = viewModel::onDismissDeleteRecord,
            onFinishClick = viewModel::onFinishClick,
            onConfirmFinish = viewModel::onConfirmFinish,
            onDismissFinish = viewModel::onDismissFinish
        )
    }

    VaccinationScreen(uiState = uiState, actions = actions)
}