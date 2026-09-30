package com.example.gash.feature.activation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.gash.core.navigation.GashRoute

fun NavGraphBuilder.activationScreen(onActivated: () -> Unit) {
    composable<GashRoute.Activation> {
        ActivationRoute(onActivated = onActivated)
    }
}