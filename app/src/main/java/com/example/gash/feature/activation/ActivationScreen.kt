package com.example.gash.feature.activation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ActivationRoute(
    onActivated: () -> Unit,
    viewModel: ActivationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ActivationEvent.NavigateToHome -> onActivated()
            }
        }
    }

    when (uiState.step) {

        ActivationStep.Form -> {
            ActivationFormContent(
                uiState = uiState,
                onFarmNameChange = viewModel::onFarmNameChange,
                onFarmIdChange = viewModel::onFarmIdChange,
                onPhoneNumberChange = viewModel::onPhoneNumberChange,
                onRequestCode = viewModel::onRequestCode
            )
        }

        ActivationStep.CodeEntry -> {
            ActivationCodeContent(
                uiState = uiState,
                onCodeChange = viewModel::onCodeChange,
                onSubmitCode = viewModel::onSubmitCode,
                onResendCode = viewModel::onResendCode,
                onEditClick = viewModel::onEditClick,
                onCodeFocus = viewModel::onCodeFocus,
            )
        }
    }
}