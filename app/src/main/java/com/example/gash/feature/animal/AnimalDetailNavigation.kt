package com.example.gash.feature.animal

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.gash.core.navigation.GashRoute

fun NavGraphBuilder.animalDetailScreen(onBack: () -> Unit) {
    composable<GashRoute.AnimalDetail> {
        AnimalDetailRoute(onBack = onBack)
    }
}