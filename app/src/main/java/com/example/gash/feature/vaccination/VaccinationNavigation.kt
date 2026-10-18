// VaccinationNavigation.kt
package com.example.gash.feature.vaccination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.gash.core.navigation.GashRoute

fun NavGraphBuilder.vaccinationScreen(
    onBack: () -> Unit,
    onAnimalClick: (Long) -> Unit,
    onNavigateToVaccineSettings: () -> Unit
) {
    composable<GashRoute.Vaccination> {
        VaccinationRoute(
            onBack = onBack,
            onAnimalClick = onAnimalClick,
            onNavigateToVaccineSettings = onNavigateToVaccineSettings
        )
    }
}