package com.snoutscout.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: Route
)

val clientTabs = listOf(
    BottomNavItem("Home", Icons.Outlined.Home, Route.ClientHome),
    BottomNavItem("Browse", Icons.Outlined.Search, Route.BrowseTrainers),
    BottomNavItem("My Dogs", Icons.Outlined.Pets, Route.DogsList),
    BottomNavItem("Wallet", Icons.Outlined.AccountBalanceWallet, Route.Wallet),
    BottomNavItem("Profile", Icons.Outlined.Person, Route.Settings)
)

val trainerTabs = listOf(
    BottomNavItem("Dashboard", Icons.Outlined.Dashboard, Route.TrainerDashboard),
    BottomNavItem("Calls", Icons.Outlined.Phone, Route.TrainerUpcoming),
    BottomNavItem("Notes", Icons.Outlined.Description, Route.TrainerNotes),
    BottomNavItem("Earnings", Icons.Outlined.AttachMoney, Route.TrainerEarnings),
    BottomNavItem("Profile", Icons.Outlined.Person, Route.TrainerProfileMgmt)
)
