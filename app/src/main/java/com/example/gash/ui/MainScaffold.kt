package com.example.gash.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gash.R
import com.example.gash.core.navigation.GashRoute
import com.example.gash.feature.account.accountScreen
import com.example.gash.feature.filetransfer.fileTransferScreen
import com.example.gash.feature.home.homeScreen
import com.example.gash.ui.theme.GashOrange

@Composable
fun MainScaffold(onNavigateToHerdDetail: (Long) -> Unit) {
    val navController = rememberNavController()
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = { MainBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = GashRoute.Home,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            homeScreen(onNavigateToHerdDetail = onNavigateToHerdDetail)
            accountScreen()
            fileTransferScreen()
        }
    }
}
@Composable
private fun MainBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = GashOrange,
        selectedTextColor = GashOrange,
        indicatorColor = Color.Transparent
    )

    NavigationBar {
        NavigationBarItem(
            selected = currentDestination.isRoute<GashRoute.Home>(),
            onClick = { navController.navigateToMainDestination(GashRoute.Home) },
            icon = { Icon(painter = painterResource(R.drawable.icon_home), contentDescription = null) },
            label = { Text(stringResource(R.string.bottom_nav_operations)) },
            colors = itemColors
        )
        NavigationBarItem(
            selected = currentDestination.isRoute<GashRoute.Account>(),
            onClick = { navController.navigateToMainDestination(GashRoute.Account) },
            icon = { Icon(painter = painterResource(R.drawable.icon_account_circle), contentDescription = null) },
            label = { Text(stringResource(R.string.bottom_nav_account)) },
            colors = itemColors
        )
        NavigationBarItem(
            selected = currentDestination.isRoute<GashRoute.FileTransfer>(),
            onClick = { navController.navigateToMainDestination(GashRoute.FileTransfer) },
            icon = { Icon(painter = painterResource(R.drawable.icon_share), contentDescription = null) },
            label = { Text(stringResource(R.string.bottom_nav_file_transfer)) },
            colors = itemColors
        )
    }
}

private inline fun <reified T : Any> androidx.navigation.NavDestination?.isRoute(): Boolean =
    this?.hierarchy?.any { it.hasRoute(T::class) } == true


private fun NavHostController.navigateToMainDestination(route: GashRoute) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}