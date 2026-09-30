package com.example.gash.feature.welcome

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.gash.core.navigation.GashRoute

fun NavGraphBuilder.welcomeScreen(onFinished: () -> Unit) {
    composable<GashRoute.Welcome> {
        WelcomeScreen(onFinished = onFinished)
    }
}