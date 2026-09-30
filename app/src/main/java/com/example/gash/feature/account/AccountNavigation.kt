package com.example.gash.feature.account

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.gash.core.navigation.GashRoute

fun NavGraphBuilder.accountScreen() {
    composable<GashRoute.Account> {
        AccountScreen()
    }
}