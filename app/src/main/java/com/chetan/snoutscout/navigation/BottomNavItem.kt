package com.chetan.snoutscout.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

val clientBottomNavItems = listOf(
    BottomNavItem("Home", AppRoute.ClientHome.route, Icons.Outlined.Home),
    BottomNavItem("Trainers", AppRoute.Browse.route, Icons.Outlined.Search),
    BottomNavItem("Dogs", AppRoute.Dogs.route, Icons.Outlined.Pets),
    BottomNavItem("Wallet", AppRoute.Wallet.route, Icons.Outlined.AccountBalanceWallet),
    BottomNavItem("Alerts", AppRoute.Notifications.route, Icons.Outlined.Notifications)
)