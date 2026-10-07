package com.example.gash.feature.animal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AnimalDetailRoute(
    onBack: () -> Unit,
    viewModel: AnimalDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AnimalDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onRecordWeightClick = viewModel::onRecordWeightClick,
        onDismissRecordWeight = viewModel::onDismissRecordWeight,
        onConfirmRecordWeight = viewModel::onConfirmRecordWeight,
        onMoveHerdClick = viewModel::onMoveHerdClick,
        onDismissMoveHerd = viewModel::onDismissMoveHerd,
        onHerdSelected = viewModel::onHerdSelected,
        onExpireClick = viewModel::onExpireClick,
        onDismissExpireConfirm = viewModel::onDismissExpireConfirm,
        onConfirmExpire = viewModel::onConfirmExpire,
        onRestoreClick = viewModel::onRestoreClick
    )
}