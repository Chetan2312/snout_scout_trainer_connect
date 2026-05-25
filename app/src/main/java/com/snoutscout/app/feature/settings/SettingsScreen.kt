package com.snoutscout.app.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.data.model.User
import com.snoutscout.app.data.model.UserRole

@Composable
fun SettingsScreen(
    user: User,
    currentRole: UserRole,
    onSwitchRole: () -> Unit,
    onSignOut: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // Profile card
        SSCard(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                SSAvatar(initials = user.name.split(" ").take(2).joinToString("") { it.first().toString() }, size = 56)
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(user.name, style = MaterialTheme.typography.headlineSmall)
                    Text(user.phone, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(user.city, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                OutlinedButton(onClick = {}, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)) {
                    Text("Edit")
                }
            }
        }

        // Role switcher
        SSCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), onClick = onSwitchRole) {
            Row(Modifier.padding(16.dp).background(MaterialTheme.colorScheme.primary.copy(0.05f)), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.SwapHoriz, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(12.dp))
                Text(
                    if (currentRole == UserRole.CLIENT) "Switch to Trainer Mode" else "Switch to Client Mode",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(Modifier.weight(1f))
                Icon(Icons.Outlined.ChevronRight, null)
            }
        }

        Spacer(Modifier.height(8.dp))

        val menuItems = listOf(
            Triple(Icons.Outlined.History, "Session History", onNavigateToHistory as () -> Unit),
            Triple(Icons.Outlined.Description, "My Reports", onNavigateToReports),
            Triple(Icons.Outlined.Star, "My Reviews", {}),
            Triple(Icons.Outlined.Notifications, "Notifications", onNavigateToNotifications),
            Triple(Icons.Outlined.PrivacyTip, "Privacy & Data Policy", {}),
            Triple(Icons.Outlined.RecordVoiceOver, "Consent Settings", {}),
            Triple(Icons.Outlined.BugReport, "Report a Problem", {}),
            Triple(Icons.Outlined.Help, "Help & Support", {})
        )

        menuItems.forEach { (icon, label, action) ->
            ListItem(
                modifier = Modifier.clickable { action() },
                leadingContent = {
                    Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                },
                headlineContent = { Text(label) },
                trailingContent = { Icon(Icons.Outlined.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }

        Spacer(Modifier.height(8.dp))

        ListItem(
            modifier = Modifier.clickable { onSignOut() },
            leadingContent = { Icon(Icons.Outlined.Logout, null, tint = SnoutScoutColors.Error) },
            headlineContent = { Text("Sign Out", color = SnoutScoutColors.Error) }
        )
    }
}

