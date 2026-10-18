package com.example.gash.feature.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.gash.core.navigation.GashRoute

fun NavGraphBuilder.homeScreen(
    onNavigateToHerdDetail: (Long) -> Unit,
    onOperationClick: (HomeOperation) -> Unit
) {
    composable<GashRoute.Home> {
        HomeRoute(
            onNavigateToHerdDetail = onNavigateToHerdDetail,
            onOperationClick = onOperationClick
        )
    }
}