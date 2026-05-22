package com.chetan.snoutscout.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chetan.snoutscout.app.AppRole
import com.chetan.snoutscout.core.ui.components.AppTopBar
import com.chetan.snoutscout.feature.client_home.ClientHomeScreen
import com.chetan.snoutscout.feature.dog_profile.DogProfilesScreen
import com.chetan.snoutscout.feature.history.SessionHistoryScreen
import com.chetan.snoutscout.feature.notifications.NotificationsScreen
import com.chetan.snoutscout.feature.onboarding.OnboardingScreen
import com.chetan.snoutscout.feature.reports.ReportsListScreen
import com.chetan.snoutscout.feature.settings.SettingsScreen
import com.chetan.snoutscout.feature.trainer_browse.BrowseTrainersScreen
import com.chetan.snoutscout.feature.trainer_dashboard.TrainerDashboardScreen
import com.chetan.snoutscout.feature.wallet.WalletScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    var currentRole by remember { mutableStateOf(AppRole.CLIENT) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        AppRoute.ClientHome.route,
        AppRoute.Browse.route,
        AppRoute.Dogs.route,
        AppRoute.Wallet.route,
        AppRoute.Notifications.route,
        AppRoute.History.route,
        AppRoute.Reports.route
    ) && currentRole == AppRole.CLIENT

    val topBarTitle = when {
        currentRole == AppRole.TRAINER && currentRoute == AppRoute.TrainerDashboard.route -> "Trainer Hub"
        currentRoute == AppRoute.Browse.route -> "Browse Trainers"
        currentRoute == AppRoute.Dogs.route -> "Your Dogs"
        currentRoute == AppRoute.Wallet.route -> "Wallet"
        currentRoute == AppRoute.Notifications.route -> "Notifications"
        currentRoute == AppRoute.History.route -> "Session History"
        currentRoute == AppRoute.Reports.route -> "Reports"
        currentRoute == AppRoute.Settings.route -> "Settings"
        currentRoute == AppRoute.ClientHome.route -> "Snout Scout"
        else -> "Snout Scout"
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = topBarTitle,
                currentRole = currentRole,
                onRoleToggle = {
                    currentRole = if (currentRole == AppRole.CLIENT) AppRole.TRAINER else AppRole.CLIENT
                    if (currentRole == AppRole.TRAINER) {
                        navController.navigate(AppRoute.TrainerDashboard.route) {
                            popUpTo(0)
                        }
                    } else {
                        navController.navigate(AppRoute.ClientHome.route) {
                            popUpTo(0)
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    clientBottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoute.Splash.route) {
                OnboardingScreen(
                    onContinue = {
                        navController.navigate(AppRoute.ClientHome.route) {
                            popUpTo(AppRoute.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(AppRoute.ClientHome.route) { ClientHomeScreen() }
            composable(AppRoute.Browse.route) { BrowseTrainersScreen() }
            composable(AppRoute.Dogs.route) { DogProfilesScreen() }
            composable(AppRoute.Wallet.route) { WalletScreen() }
            composable(AppRoute.Notifications.route) { NotificationsScreen() }
            composable(AppRoute.History.route) { SessionHistoryScreen() }
            composable(AppRoute.Reports.route) { ReportsListScreen() }
            composable(AppRoute.Settings.route) { SettingsScreen() }
            composable(AppRoute.TrainerDashboard.route) { TrainerDashboardScreen() }
        }
    }
}