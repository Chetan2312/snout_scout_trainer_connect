package com.snoutscout.app.feature.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snoutscout.app.SnoutScoutApp
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.core.util.DateFormatter
import com.snoutscout.app.data.model.CallType

@Composable
fun SessionHistoryScreen(
    onBack: () -> Unit,
    onSessionClick: (String) -> Unit
) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val viewModel: HistoryViewModel = viewModel(factory = HistoryViewModelFactory(container))
    val sessions by viewModel.sessions.collectAsStateWithLifecycle()

    Scaffold(topBar = { SSTopBar("Session History", onBack = onBack) }) { padding ->
        if (sessions.isEmpty()) {
            SSEmptyState(
                icon = Icons.Outlined.History,
                title = "No sessions yet",
                subtitle = "Your consultation sessions will appear here",
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 24.dp)) {
                items(sessions) { session ->
                    SSCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), onClick = { onSessionClick(session.id) }) {
                        Row(Modifier.padding(16.dp)) {
                            SSAvatar(initials = session.trainerName.split(" ").take(2).joinToString("") { it.first().toString() })
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Row {
                                    Text(session.trainerName, style = MaterialTheme.typography.titleSmall)
                                    Spacer(Modifier.width(6.dp))
                                    Icon(
                                        if (session.type == CallType.VIDEO) Icons.Outlined.Videocam else Icons.Outlined.Phone,
                                        null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text("${session.dogName} · ${session.durationMinutes} min · ₹${session.cost}",
                                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(Modifier.height(4.dp))
                                Row {
                                    Text(DateFormatter.formatDate(session.date), style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    if (session.hasReport) {
                                        Spacer(Modifier.width(8.dp))
                                        SSBadge("Report", type = BadgeType.SUCCESS)
                                    }
                                }
                                Text(session.summary, style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}
