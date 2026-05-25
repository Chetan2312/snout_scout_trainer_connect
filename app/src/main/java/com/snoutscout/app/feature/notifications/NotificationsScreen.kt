package com.snoutscout.app.feature.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snoutscout.app.SnoutScoutApp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.SSTopBar
import com.snoutscout.app.core.util.DateFormatter
import com.snoutscout.app.data.model.NotificationItem
import com.snoutscout.app.data.model.NotificationType
import com.snoutscout.app.data.repository.NotificationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val notifications by container.notificationRepository.getNotifications()
        .let { flow ->
            val vm = androidx.lifecycle.viewmodel.compose.viewModel<NotificationListVM>(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(c: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return NotificationListVM(container.notificationRepository) as T
                    }
                }
            )
            vm.notifications.collectAsStateWithLifecycle()
        }

    Scaffold(topBar = { SSTopBar("Notifications", onBack = onBack) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            items(notifications) { notif ->
                NotificationRow(notif)
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun NotificationRow(notif: NotificationItem) {
    ListItem(
        modifier = Modifier.background(
            if (!notif.isRead) MaterialTheme.colorScheme.primary.copy(0.05f)
            else MaterialTheme.colorScheme.background
        ),
        leadingContent = {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(notif.type.icon(), null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
            }
        },
        headlineContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(notif.title, style = MaterialTheme.typography.titleSmall)
                if (!notif.isRead) {
                    Spacer(Modifier.width(6.dp))
                    Box(Modifier.size(8.dp).clip(CircleShape).background(SnoutScoutColors.Primary))
                }
            }
        },
        supportingContent = {
            Column {
                Text(notif.body, style = MaterialTheme.typography.bodySmall)
                Text(DateFormatter.timeAgo(notif.date), style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

private fun NotificationType.icon(): ImageVector = when (this) {
    NotificationType.REPORT -> Icons.Outlined.Description
    NotificationType.REMINDER -> Icons.Outlined.Schedule
    NotificationType.PROMO -> Icons.Outlined.Bolt
    NotificationType.SESSION -> Icons.Outlined.Phone
}

class NotificationListVM(repo: NotificationRepository) : androidx.lifecycle.ViewModel() {
    val notifications = repo.getNotifications()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
