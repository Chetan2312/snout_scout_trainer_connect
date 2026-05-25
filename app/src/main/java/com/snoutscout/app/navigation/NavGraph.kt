package com.snoutscout.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.snoutscout.app.AppViewModel
import com.snoutscout.app.data.model.UserRole
import com.snoutscout.app.feature.auth.LoginScreen
import com.snoutscout.app.feature.auth.OnboardingScreen
import com.snoutscout.app.feature.auth.SplashScreen
import com.snoutscout.app.feature.call.ActiveCallScreen
import com.snoutscout.app.feature.call.CallPreCheckScreen
import com.snoutscout.app.feature.call.PostCallScreen
import com.snoutscout.app.feature.call.ScheduleBookingScreen
import com.snoutscout.app.feature.chat.ChatScreen
import com.snoutscout.app.feature.client_home.HomeScreen
import com.snoutscout.app.feature.dog_profile.DogFormScreen
import com.snoutscout.app.feature.dog_profile.DogsListScreen
import com.snoutscout.app.feature.earnings.TrainerEarningsScreen
import com.snoutscout.app.feature.earnings.TrainerWithdrawScreen
import com.snoutscout.app.feature.history.SessionDetailScreen
import com.snoutscout.app.feature.history.SessionHistoryScreen
import com.snoutscout.app.feature.notifications.NotificationsScreen
import com.snoutscout.app.feature.reports.ReportDetailScreen
import com.snoutscout.app.feature.reports.ReportsListScreen
import com.snoutscout.app.feature.settings.SettingsScreen
import com.snoutscout.app.feature.trainer_availability.TrainerAvailabilityScreen
import com.snoutscout.app.feature.trainer_browse.BrowseTrainersScreen
import com.snoutscout.app.feature.trainer_dashboard.TrainerDashboardScreen
import com.snoutscout.app.feature.trainer_notes.TrainerNotesScreen
import com.snoutscout.app.feature.trainer_profile.TrainerProfileScreen
import com.snoutscout.app.feature.trainer_profile_mgmt.TrainerProfileMgmtScreen
import com.snoutscout.app.feature.trainer_upcoming.TrainerUpcomingScreen
import com.snoutscout.app.feature.wallet.WalletScreen

@Composable
fun NavGraph(appViewModel: AppViewModel = viewModel()) {
    val navController = rememberNavController()
    val appState by appViewModel.appState.collectAsStateWithLifecycle()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val callRoutes = setOf(Route.ActiveCall.route, Route.CallPreCheck.route, Route.PostCall.route)
    val authRoutes = setOf(Route.Splash.route, Route.Onboarding.route, Route.Login.route)
    val showBottomNav = currentRoute != null &&
            !callRoutes.any { currentRoute.startsWith(it.split("{")[0]) } &&
            !authRoutes.contains(currentRoute)

    val tabs = if (appState.currentRole == UserRole.CLIENT) clientTabs else trainerTabs

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                NavigationBar {
                    val navBackStack by navController.currentBackStackEntryAsState()
                    val currentDest = navBackStack?.destination
                    tabs.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDest?.hierarchy?.any { it.route == item.route.route } == true,
                            onClick = {
                                navController.navigate(item.route.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Route.Splash.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Route.Splash.route) {
                SplashScreen(onComplete = { navController.navigate(Route.Onboarding.route) { popUpTo(Route.Splash.route) { inclusive = true } } })
            }
            composable(Route.Onboarding.route) {
                OnboardingScreen(onGetStarted = { navController.navigate(Route.Login.route) { popUpTo(Route.Onboarding.route) { inclusive = true } } })
            }
            composable(Route.Login.route) {
                LoginScreen(onLogin = {
                    appViewModel.login()
                    navController.navigate(Route.ClientHome.route) { popUpTo(Route.Login.route) { inclusive = true } }
                })
            }

            // Client tabs
            composable(Route.ClientHome.route) {
                HomeScreen(
                    walletBalance = appState.walletBalance,
                    userName = appState.currentUser.name,
                    onNavigateToTrainer = { navController.navigate(Route.TrainerProfile.createRoute(it)) },
                    onNavigateToBrowse = { navController.navigate(Route.BrowseTrainers.route) },
                    onNavigateToDogs = { navController.navigate(Route.DogsList.route) },
                    onNavigateToWallet = { navController.navigate(Route.Wallet.route) },
                    onNavigateToHistory = { navController.navigate(Route.SessionHistory.route) },
                    onNavigateToReports = { navController.navigate(Route.ReportsList.route) },
                    onNavigateToNotifications = { navController.navigate(Route.Notifications.route) }
                )
            }
            composable(Route.BrowseTrainers.route) {
                BrowseTrainersScreen(
                    onTrainerClick = { navController.navigate(Route.TrainerProfile.createRoute(it)) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Route.DogsList.route) {
                DogsListScreen(
                    onAddDog = { navController.navigate(Route.AddDog.route) },
                    onEditDog = { navController.navigate(Route.EditDog.createRoute(it)) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Route.Wallet.route) {
                WalletScreen(onBack = { navController.popBackStack() })
            }
            composable(Route.Settings.route) {
                SettingsScreen(
                    user = appState.currentUser,
                    currentRole = appState.currentRole,
                    onSwitchRole = {
                        appViewModel.switchRole()
                        val dest = if (appState.currentRole == UserRole.CLIENT) Route.TrainerDashboard.route else Route.ClientHome.route
                        navController.navigate(dest) { popUpTo(0) { inclusive = false } }
                    },
                    onSignOut = {
                        appViewModel.logout()
                        navController.navigate(Route.Splash.route) { popUpTo(0) { inclusive = true } }
                    },
                    onNavigateToHistory = { navController.navigate(Route.SessionHistory.route) },
                    onNavigateToReports = { navController.navigate(Route.ReportsList.route) },
                    onNavigateToNotifications = { navController.navigate(Route.Notifications.route) }
                )
            }

            // Trainer profile
            composable(
                Route.TrainerProfile.route,
                arguments = listOf(navArgument("trainerId") { type = NavType.StringType })
            ) { back ->
                val trainerId = back.arguments?.getString("trainerId") ?: ""
                TrainerProfileScreen(
                    trainerId = trainerId,
                    walletBalance = appState.walletBalance,
                    onBack = { navController.popBackStack() },
                    onCallNow = { id, type -> navController.navigate(Route.CallPreCheck.createRoute(id, type)) },
                    onSchedule = { id -> navController.navigate(Route.ScheduleBooking.createRoute(id)) }
                )
            }

            // Call flow
            composable(
                Route.CallPreCheck.route,
                arguments = listOf(
                    navArgument("trainerId") { type = NavType.StringType },
                    navArgument("callType") { type = NavType.StringType }
                )
            ) { back ->
                val trainerId = back.arguments?.getString("trainerId") ?: ""
                val callType = back.arguments?.getString("callType") ?: "VOICE"
                CallPreCheckScreen(
                    trainerId = trainerId,
                    callType = callType,
                    walletBalance = appState.walletBalance,
                    onBack = { navController.popBackStack() },
                    onStartCall = { tid, did, ct ->
                        navController.navigate(Route.ActiveCall.createRoute(tid, did, ct))
                    },
                    onRecharge = { navController.navigate(Route.Wallet.route) }
                )
            }
            composable(
                Route.ActiveCall.route,
                arguments = listOf(
                    navArgument("trainerId") { type = NavType.StringType },
                    navArgument("dogId") { type = NavType.StringType },
                    navArgument("callType") { type = NavType.StringType }
                )
            ) { back ->
                val trainerId = back.arguments?.getString("trainerId") ?: ""
                val dogId = back.arguments?.getString("dogId") ?: ""
                val callType = back.arguments?.getString("callType") ?: "VOICE"
                ActiveCallScreen(
                    trainerId = trainerId,
                    dogId = dogId,
                    callType = callType,
                    walletBalance = appState.walletBalance,
                    onCallEnd = { elapsed, cost ->
                        navController.navigate(Route.PostCall.createRoute(trainerId, elapsed, cost)) {
                            popUpTo(Route.ActiveCall.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                Route.PostCall.route,
                arguments = listOf(
                    navArgument("trainerId") { type = NavType.StringType },
                    navArgument("elapsed") { type = NavType.IntType },
                    navArgument("cost") { type = NavType.IntType }
                )
            ) { back ->
                val trainerId = back.arguments?.getString("trainerId") ?: ""
                val elapsed = back.arguments?.getInt("elapsed") ?: 0
                val cost = back.arguments?.getInt("cost") ?: 0
                PostCallScreen(
                    trainerId = trainerId,
                    elapsedSeconds = elapsed,
                    cost = cost,
                    onHome = { navController.navigate(Route.ClientHome.route) { popUpTo(0) { inclusive = false } } },
                    onHistory = { navController.navigate(Route.SessionHistory.route) }
                )
            }
            composable(
                Route.ScheduleBooking.route,
                arguments = listOf(navArgument("trainerId") { type = NavType.StringType })
            ) { back ->
                val trainerId = back.arguments?.getString("trainerId") ?: ""
                ScheduleBookingScreen(trainerId = trainerId, onBack = { navController.popBackStack() })
            }

            // Dogs
            composable(Route.AddDog.route) {
                DogFormScreen(dogId = null, onBack = { navController.popBackStack() })
            }
            composable(
                Route.EditDog.route,
                arguments = listOf(navArgument("dogId") { type = NavType.StringType })
            ) { back ->
                val dogId = back.arguments?.getString("dogId") ?: ""
                DogFormScreen(dogId = dogId, onBack = { navController.popBackStack() })
            }

            // History & Reports
            composable(Route.SessionHistory.route) {
                SessionHistoryScreen(
                    onBack = { navController.popBackStack() },
                    onSessionClick = { navController.navigate(Route.SessionDetail.createRoute(it)) }
                )
            }
            composable(
                Route.SessionDetail.route,
                arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
            ) { back ->
                val sessionId = back.arguments?.getString("sessionId") ?: ""
                SessionDetailScreen(
                    sessionId = sessionId,
                    onBack = { navController.popBackStack() },
                    onViewReport = { navController.navigate(Route.ReportDetail.createRoute(it)) },
                    onOpenChat = { navController.navigate(Route.Chat.createRoute(it)) }
                )
            }
            composable(Route.ReportsList.route) {
                ReportsListScreen(
                    onBack = { navController.popBackStack() },
                    onReportClick = { navController.navigate(Route.ReportDetail.createRoute(it)) }
                )
            }
            composable(
                Route.ReportDetail.route,
                arguments = listOf(navArgument("reportId") { type = NavType.StringType })
            ) { back ->
                val reportId = back.arguments?.getString("reportId") ?: ""
                ReportDetailScreen(reportId = reportId, onBack = { navController.popBackStack() })
            }
            composable(
                Route.Chat.route,
                arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
            ) { back ->
                val sessionId = back.arguments?.getString("sessionId") ?: ""
                ChatScreen(sessionId = sessionId, onBack = { navController.popBackStack() })
            }
            composable(Route.Notifications.route) {
                NotificationsScreen(onBack = { navController.popBackStack() })
            }

            // Trainer tabs
            composable(Route.TrainerDashboard.route) {
                TrainerDashboardScreen(
                    onNavigateToProfile = { navController.navigate(Route.TrainerProfileMgmt.route) },
                    onNavigateToAvailability = { navController.navigate(Route.TrainerAvailability.route) },
                    onNavigateToNotes = { navController.navigate(Route.TrainerNotes.route) },
                    onNavigateToEarnings = { navController.navigate(Route.TrainerEarnings.route) }
                )
            }
            composable(Route.TrainerUpcoming.route) {
                TrainerUpcomingScreen()
            }
            composable(Route.TrainerNotes.route) {
                TrainerNotesScreen(onBack = { navController.popBackStack() })
            }
            composable(Route.TrainerEarnings.route) {
                TrainerEarningsScreen(
                    onBack = { navController.popBackStack() },
                    onWithdraw = { navController.navigate(Route.TrainerWithdraw.route) }
                )
            }
            composable(Route.TrainerProfileMgmt.route) {
                TrainerProfileMgmtScreen(onBack = { navController.popBackStack() })
            }
            composable(Route.TrainerAvailability.route) {
                TrainerAvailabilityScreen(onBack = { navController.popBackStack() })
            }
            composable(Route.TrainerWithdraw.route) {
                TrainerWithdrawScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
