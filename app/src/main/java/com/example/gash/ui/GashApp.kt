package com.example.gash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gash.core.navigation.GashRoute
import com.example.gash.feature.activation.activationScreen
import com.example.gash.feature.animal.animalDetailScreen
import com.example.gash.feature.herd.herdDetailScreen
import com.example.gash.feature.welcome.welcomeScreen

@Composable
fun GashApp(viewModel: GashAppViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val isActivated by viewModel.isActivated.collectAsStateWithLifecycle()
    var welcomeFinished by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(welcomeFinished, isActivated) {
        if (welcomeFinished && isActivated != null) {
            val destination = if (isActivated == true) GashRoute.Main else GashRoute.Activation
            navController.navigate(destination) {
                popUpTo(GashRoute.Welcome) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = GashRoute.Welcome
    ) {
        welcomeScreen(onFinished = { welcomeFinished = true })

        activationScreen(
            onActivated = {
                navController.navigate(GashRoute.Main) {
                    popUpTo(GashRoute.Activation) { inclusive = true }
                }
            }
        )

        composable<GashRoute.Main> {
            MainScaffold(
                onNavigateToHerdDetail = { herdId -> navController.navigate(GashRoute.HerdDetail(herdId)) }
            )
        }

        herdDetailScreen(
            onBack = { navController.popBackStack() },
            onAnimalClick = { animalId -> navController.navigate(GashRoute.AnimalDetail(animalId)) }
        )

        animalDetailScreen(
            onBack = { navController.popBackStack() }
        )
    }
}