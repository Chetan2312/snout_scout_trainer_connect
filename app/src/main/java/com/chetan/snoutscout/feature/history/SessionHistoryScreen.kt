package com.chetan.snoutscout.feature.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chetan.snoutscout.SnoutScoutApp
import com.chetan.snoutscout.core.ui.components.EmptyStateCard

@Composable
fun SessionHistoryScreen() {
    val application = LocalContext.current.applicationContext as SnoutScoutApp
    val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModel.factory(application.appContainer.sessionRepository)
    )
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Session history",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        items(uiState.sessions, key = { it.id }) { session ->
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${session.trainerName} • ${session.dogName}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${session.callType.name} • ${session.durationMinutes} min • ₹${session.totalAmountInInr}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = session.scheduledAt,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = if (session.notesReady) "Report available" else "Report pending",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        if (uiState.sessions.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "No sessions yet",
                    subtitle = "Completed consultations will appear here with billing and report status."
                )
            }
        }
    }
}