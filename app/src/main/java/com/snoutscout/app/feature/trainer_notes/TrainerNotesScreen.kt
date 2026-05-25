package com.snoutscout.app.feature.trainer_notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
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
import com.snoutscout.app.data.model.SessionReport

@Composable
fun TrainerNotesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val viewModel: TrainerNotesViewModel = viewModel(factory = TrainerNotesViewModelFactory(container))
    val reports by viewModel.reports.collectAsStateWithLifecycle()
    var selectedReport by remember { mutableStateOf<SessionReport?>(null) }

    if (selectedReport != null) {
        ReportEditView(
            report = selectedReport!!,
            onBack = { selectedReport = null },
            onApprove = { report ->
                viewModel.approveReport(report)
                selectedReport = null
            },
            onSave = { report ->
                viewModel.saveReport(report)
                selectedReport = null
            }
        )
    } else {
        Scaffold(topBar = { SSTopBar("Consultation Notes", onBack = onBack) }) { padding ->
            if (reports.isEmpty()) {
                SSEmptyState(
                    icon = Icons.Outlined.Description,
                    title = "No notes yet",
                    subtitle = "Your consultation notes will appear here",
                    modifier = Modifier.fillMaxSize().padding(padding)
                )
            } else {
                LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 24.dp)) {
                    items(reports) { report ->
                        SSCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), onClick = { selectedReport = report }) {
                            Row(Modifier.padding(16.dp)) {
                                Icon(Icons.Outlined.Description, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(report.dogName, style = MaterialTheme.typography.titleSmall)
                                    Text(report.issueDiscussed.take(50) + "...", style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(report.date, style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                SSBadge(
                                    if (report.status == ReportStatus.APPROVED) "Approved" else "Review",
                                    type = if (report.status == ReportStatus.APPROVED) BadgeType.SUCCESS else BadgeType.WARNING
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportEditView(
    report: SessionReport,
    onBack: () -> Unit,
    onApprove: (SessionReport) -> Unit,
    onSave: (SessionReport) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var issueDiscussed by remember { mutableStateOf(report.issueDiscussed) }
    var observations by remember { mutableStateOf(report.observations) }
    var solutions by remember { mutableStateOf(report.solutions) }
    var routine by remember { mutableStateOf(report.routine) }
    var instructions by remember { mutableStateOf(report.instructions) }
    var followUp by remember { mutableStateOf(report.followUp) }

    Scaffold(
        topBar = {
            SSTopBar(
                title = "Session Report",
                onBack = onBack,
                actions = {}
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SSCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    SSBadge("AI-Generated Draft", type = BadgeType.PRIMARY)
                    Spacer(Modifier.height(8.dp))
                    Text("${report.dogName} — ${report.date}", style = MaterialTheme.typography.titleSmall)
                }
            }
            EditableSection("ISSUE DISCUSSED", issueDiscussed, isEditing) { issueDiscussed = it }
            EditableSection("TRAINER OBSERVATIONS", observations, isEditing) { observations = it }
            EditableSection("SUGGESTED SOLUTIONS", solutions, isEditing) { solutions = it }
            EditableSection("DAILY ROUTINE", routine, isEditing) { routine = it }
            EditableSection("TRAINING INSTRUCTIONS", instructions, isEditing) { instructions = it }
            EditableSection("FOLLOW-UP", followUp, isEditing) { followUp = it }

            if (isEditing) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SSButton("Cancel", onClick = { isEditing = false }, modifier = Modifier.weight(1f), variant = ButtonVariant.SECONDARY)
                    SSButton("Save Changes", onClick = {
                        onSave(report.copy(issueDiscussed = issueDiscussed, observations = observations,
                            solutions = solutions, routine = routine, instructions = instructions, followUp = followUp))
                    }, modifier = Modifier.weight(1f))
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SSButton("Edit", onClick = { isEditing = true }, modifier = Modifier.weight(1f), variant = ButtonVariant.SECONDARY)
                    SSButton("Approve & Send", onClick = {
                        onApprove(report.copy(status = ReportStatus.APPROVED))
                    }, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun EditableSection(label: String, value: String, isEditing: Boolean, onValueChange: (String) -> Unit) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary,
            letterSpacing = androidx.compose.ui.unit.TextUnit(1.5f, androidx.compose.ui.unit.TextUnitType.Sp))
        Spacer(Modifier.height(4.dp))
        if (isEditing) {
            OutlinedTextField(
                value = value, onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(), minLines = 2,
                shape = RoundedCornerShape(8.dp)
            )
        } else {
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
