package com.snoutscout.app.feature.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.*


@Composable
fun ReportDetailScreen(reportId: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val viewModel: ReportsViewModel = viewModel(factory = ReportsViewModelFactory(container))
    val reports by viewModel.reports.collectAsStateWithLifecycle()
    val report = remember(reports) { reports.find { it.id == reportId } }

    Scaffold(
        topBar = {
            SSTopBar("Consultation Report", onBack = onBack, actions = {
                IconButton(onClick = {}) { Icon(Icons.Outlined.Share, "Share") }
                IconButton(onClick = {}) { Icon(Icons.Outlined.Download, "Download") }
            })
        }
    ) { padding ->
        report?.let { r ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SSCard(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Row {
                            Column(Modifier.weight(1f)) {
                                Text(r.dogName, style = MaterialTheme.typography.titleMedium)
                                Text("with ${r.trainerName}", style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(r.date, style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            SSBadge("AI-Generated", type = BadgeType.PRIMARY)
                        }
                    }
                }
                ReportSection("ISSUE DISCUSSED", r.issueDiscussed)
                ReportSection("TRAINER OBSERVATIONS", r.observations)
                ReportSection("SUGGESTED SOLUTIONS", r.solutions)
                ReportSection("DAILY ROUTINE", r.routine)
                ReportSection("TRAINING INSTRUCTIONS", r.instructions)
                ReportSection("FOLLOW-UP RECOMMENDATIONS", r.followUp)
                SSCard(modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(16.dp)) {
                        Icon(Icons.Outlined.Shield, null, tint = SnoutScoutColors.Primary, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("This report was generated using AI based on session notes and trainer observations.",
                            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportSection(label: String, content: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = SnoutScoutColors.Primary,
            letterSpacing = androidx.compose.ui.unit.TextUnit(1.5f, androidx.compose.ui.unit.TextUnitType.Sp))
        Spacer(Modifier.height(6.dp))
        Text(content, style = MaterialTheme.typography.bodyMedium)
    }
}
