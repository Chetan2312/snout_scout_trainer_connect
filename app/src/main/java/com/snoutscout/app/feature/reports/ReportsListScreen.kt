package com.snoutscout.app.feature.reports

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
import com.snoutscout.app.data.model.ReportStatus

@Composable
fun ReportsListScreen(onBack: () -> Unit, onReportClick: (String) -> Unit) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val viewModel: ReportsViewModel = viewModel(factory = ReportsViewModelFactory(container))
    val reports by viewModel.reports.collectAsStateWithLifecycle()

    Scaffold(topBar = { SSTopBar("My Reports", onBack = onBack) }) { padding ->
        if (reports.isEmpty()) {
            SSEmptyState(
                icon = Icons.Outlined.Description,
                title = "No reports yet",
                subtitle = "Your AI-generated session reports will appear here",
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 24.dp)) {
                items(reports) { report ->
                    SSCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), onClick = { onReportClick(report.id) }) {
                        Row(Modifier.padding(16.dp)) {
                            Icon(Icons.Outlined.Description, null, tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp))
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(report.dogName, style = MaterialTheme.typography.titleSmall)
                                Text("with ${report.trainerName}", style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(report.date, style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            SSBadge(
                                if (report.status == ReportStatus.APPROVED) "Approved" else "Pending",
                                type = if (report.status == ReportStatus.APPROVED) BadgeType.SUCCESS else BadgeType.WARNING
                            )
                        }
                    }
                }
            }
        }
    }
}
