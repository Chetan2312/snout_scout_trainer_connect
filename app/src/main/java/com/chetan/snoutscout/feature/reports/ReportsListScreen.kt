package com.chetan.snoutscout.feature.reports

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
import com.chetan.snoutscout.core.ui.components.PrimaryButton

@Composable
fun ReportsListScreen() {
    val application = LocalContext.current.applicationContext as SnoutScoutApp
    val viewModel: ReportsViewModel = viewModel(
        factory = ReportsViewModel.factory(application.appContainer.reportRepository)
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
                text = "Consultation reports",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        items(uiState.reports, key = { it.id }) { report ->
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = report.dogIssueDiscussed,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = report.trainerObservations,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = if (report.approved) "Approved by trainer" else "Draft pending approval",
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (!report.approved) {
                        PrimaryButton(
                            text = "Approve report",
                            onClick = { viewModel.approveReport(report.id) }
                        )
                    }
                }
            }
        }

        if (uiState.reports.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "No reports available",
                    subtitle = "Approved consultation reports will appear here for review and sharing."
                )
            }
        }
    }
}