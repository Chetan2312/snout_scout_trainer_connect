package com.snoutscout.app.feature.history

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snoutscout.app.SnoutScoutApp
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.core.util.DateFormatter
import com.snoutscout.app.data.model.ConsultationSession

@Composable
fun SessionDetailScreen(
    sessionId: String,
    onBack: () -> Unit,
    onViewReport: (String) -> Unit,
    onOpenChat: (String) -> Unit
) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val viewModel: HistoryViewModel = viewModel(factory = HistoryViewModelFactory(container))
    val sessions by viewModel.sessions.collectAsStateWithLifecycle()

    // Find session from DAO-backed list
    val session = remember(sessions) { sessions.find { it.id == sessionId } }

    Scaffold(topBar = { SSTopBar("Session Detail", onBack = onBack) }) { padding ->
        session?.let { s ->
            Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SSCard(modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        SSAvatar(initials = s.trainerName.split(" ").take(2).joinToString("") { it.first().toString() }, size = 48)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(s.trainerName, style = MaterialTheme.typography.titleMedium)
                            Text(DateFormatter.formatDateTime(s.date), style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                SSCard(modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${s.durationMinutes} min", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Duration", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Divider(modifier = Modifier.height(40.dp).width(1.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("₹${s.cost}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Cost", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Divider(modifier = Modifier.height(40.dp).width(1.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(s.type.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Type", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                Text(s.summary, style = MaterialTheme.typography.bodyMedium)
                if (s.hasReport) SSButton("View Report", onClick = { onViewReport(s.id) }, modifier = Modifier.fillMaxWidth())
                if (s.hasChat) SSButton("Open Chat", onClick = { onOpenChat(s.id) }, modifier = Modifier.fillMaxWidth(), variant = ButtonVariant.SECONDARY)
                SSButton("Book Again", onClick = {}, modifier = Modifier.fillMaxWidth(), variant = ButtonVariant.SECONDARY)
            }
        } ?: Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
