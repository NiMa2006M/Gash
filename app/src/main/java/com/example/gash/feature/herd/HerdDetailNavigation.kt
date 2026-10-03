package com.example.gash.feature.herd

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.gash.core.navigation.GashRoute

fun NavGraphBuilder.herdDetailScreen(
    onBack: () -> Unit,
    onAnimalClick: (Long) -> Unit
) {
    composable<GashRoute.HerdDetail> {
        HerdDetailRoute(onBack = onBack, onAnimalClick = onAnimalClick)
    }
}