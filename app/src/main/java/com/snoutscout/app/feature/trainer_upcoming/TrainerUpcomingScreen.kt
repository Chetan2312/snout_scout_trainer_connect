package com.snoutscout.app.feature.trainer_upcoming

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.core.util.DateFormatter
import com.snoutscout.app.data.model.CallType
import com.snoutscout.app.data.model.MockData

@Composable
fun TrainerUpcomingScreen() {
    Scaffold(
        topBar = { SSTopBar("Upcoming Calls") }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 24.dp)) {
            items(MockData.UPCOMING_CALLS) { call ->
                SSCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            SSAvatar(initials = call.clientName.take(2), size = 44)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(call.clientName, style = MaterialTheme.typography.titleSmall)
                                Text("${call.dogName} · ${call.breed}", style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            SSBadge(
                                if (call.type == CallType.VIDEO) "Video" else "Voice",
                                type = if (call.type == CallType.VIDEO) BadgeType.PRIMARY else BadgeType.DEFAULT
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(DateFormatter.formatDateTime(call.scheduledAt) + " · ${call.durationMinutes} min",
                            style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("📋 ${call.issue}", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
