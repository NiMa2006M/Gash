package com.example.gash.feature.account

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.gash.core.navigation.GashRoute

fun NavGraphBuilder.accountScreen(
    onNavigateToGeneralInfo: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    composable<GashRoute.Account> {
        AccountScreen(
            onNavigateToGeneralInfo = onNavigateToGeneralInfo,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToAbout = onNavigateToAbout
        )
    }
}