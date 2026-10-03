package com.example.gash.feature.herd

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HerdDetailRoute(
    onBack: () -> Unit,
    onAnimalClick: (Long) -> Unit,
    viewModel: HerdDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HerdDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onAnimalClick = onAnimalClick,
        onAddClick = viewModel::onAddClick,
        onDismissAddSheet = viewModel::onDismissAddSheet,
        onPickExistingSelected = viewModel::onPickExistingSelected,
        onRegisterNewSelected = viewModel::onRegisterNewSelected,
        onToggleUnassignedSelection = viewModel::onToggleUnassignedSelection,
        onConfirmAddExisting = viewModel::onConfirmAddExisting,
        onConfirmRegisterNew = viewModel::onConfirmRegisterNew
    )
}