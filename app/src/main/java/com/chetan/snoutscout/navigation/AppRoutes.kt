package com.chetan.snoutscout.navigation

sealed class AppRoute(val route: String) {
    data object Splash : AppRoute("splash")
    data object Welcome : AppRoute("welcome")
    data object ClientHome : AppRoute("client_home")
    data object Browse : AppRoute("browse")
    data object Dogs : AppRoute("dogs")
    data object Wallet : AppRoute("wallet")
    data object Notifications : AppRoute("notifications")
    data object Settings : AppRoute("settings")
    data object TrainerDashboard : AppRoute("trainer_dashboard")
}