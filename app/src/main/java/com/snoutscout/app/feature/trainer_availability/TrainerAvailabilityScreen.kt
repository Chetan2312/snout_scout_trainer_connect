package com.snoutscout.app.feature.trainer_availability

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.data.model.MockData

@Composable
fun TrainerAvailabilityScreen(onBack: () -> Unit) {
    var isOnline by remember { mutableStateOf(true) }

    Scaffold(topBar = { SSTopBar("Availability", onBack = onBack) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp)) {
            item {
                SSCard(modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Online Status", style = MaterialTheme.typography.titleSmall)
                            Text("Accepting calls", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(checked = isOnline, onCheckedChange = { isOnline = it })
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("Weekly Schedule", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
            }
            items(MockData.AVAILABILITY) { day ->
                SSCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            day.dayOfWeek.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.width(90.dp)
                        )
                        if (day.slots.isEmpty()) {
                            SSBadge("Day off", type = BadgeType.WARNING)
                        } else {
                            Column {
                                day.slots.forEach { slot ->
                                    SSBadge("${slot.start} – ${slot.end}", type = BadgeType.SUCCESS)
                                    Spacer(Modifier.height(4.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
