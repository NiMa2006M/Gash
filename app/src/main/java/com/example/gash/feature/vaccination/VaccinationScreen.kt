package com.example.gash.feature.vaccination

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.gash.R
import com.example.gash.core.operation.OperationHeader
import com.example.gash.core.time.formatDateTime

@Composable
fun VaccinationScreen(
    uiState: VaccinationUiState,
    actions: VaccinationActions
) {
    val locale = LocalConfiguration.current.locales[0]
    val operation = uiState.operation

    val subtitle = if (operation != null) {
        stringResource(
            R.string.vaccination_header_subtitle,
            operation.id,
            formatDateTime(operation.startedAt, locale)
        )
    } else {
        stringResource(R.string.vaccination_header_new_operation)
    }

    Column(modifier = Modifier.fillMaxSize().imePadding()) {
        OperationHeader(
            title = stringResource(R.string.vaccination_title),
            subtitle = subtitle,
            iconRes = R.drawable.gash_home_icon_vaccination,
            tabs = listOf(
                stringResource(R.string.vaccination_tab_register),
                stringResource(R.string.vaccination_tab_settings)
            ),
            selectedTabIndex = uiState.selectedTab.ordinal,
            onTabSelected = { actions.onTabSelected(VaccinationTab.entries[it]) },
            onBack = actions.onBack
        )

        when {
            uiState.isLoading -> Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            uiState.selectedTab == VaccinationTab.REGISTER ->
                VaccinationRegisterTab(uiState, actions, Modifier.weight(1f))

            else -> VaccinationSettingsTab(uiState, actions, Modifier.weight(1f))
        }
    }

    VaccinationSheetsAndDialogs(uiState, actions)
}