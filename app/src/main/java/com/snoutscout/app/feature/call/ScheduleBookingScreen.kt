package com.snoutscout.app.feature.call

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.data.model.MockData
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScheduleBookingScreen(trainerId: String, onBack: () -> Unit) {
    val trainer = remember { MockData.TRAINERS.find { it.id == trainerId } }
    var selectedDate by remember { mutableStateOf<Int?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var selectedDuration by remember { mutableStateOf<Int?>(null) }
    var selectedDogId by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val days = remember {
        val cal = Calendar.getInstance()
        (0..6).map { i ->
            cal.time.also { cal.add(Calendar.DAY_OF_YEAR, 1) }
        }
    }
    val timeSlots = listOf("09:00", "10:00", "11:00", "14:00", "15:00", "16:00", "17:00")
    val durations = listOf(15, 30, 60)

    Scaffold(
        topBar = { SSTopBar("Schedule Booking", onBack = onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            trainer?.let { t ->
                SSCard {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        SSAvatar(initials = t.name.split(" ").take(2).joinToString("") { it.first().toString() })
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(t.name, style = MaterialTheme.typography.titleMedium)
                            Text("₹${t.ratePerMin}/min", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Text("Select Date", style = MaterialTheme.typography.titleSmall)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(days.indices.toList()) { i ->
                    val cal = Calendar.getInstance().also { c -> c.add(Calendar.DAY_OF_YEAR, i) }
                    val selected = selectedDate == i
                    Box(
                        modifier = Modifier
                            .size(width = 56.dp, height = 72.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) SnoutScoutColors.Primary else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { selectedDate = i },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")[cal.get(Calendar.DAY_OF_WEEK) - 1],
                                style = MaterialTheme.typography.labelSmall,
                                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "${cal.get(Calendar.DAY_OF_MONTH)}",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Text("Select Time", style = MaterialTheme.typography.titleSmall)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                timeSlots.forEach { time ->
                    SSChip(time, selectedTime == time, { selectedTime = time })
                }
            }

            Text("Duration", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                durations.forEach { dur ->
                    SSChip("$dur min", selectedDuration == dur, { selectedDuration = dur })
                }
            }

            Text("Select Dog", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MockData.DOGS.forEach { dog ->
                    SSChip("🐕 ${dog.name}", selectedDogId == dog.id, { selectedDogId = dog.id })
                }
            }

            if (selectedTime != null && selectedDuration != null) {
                val cost = (selectedDuration ?: 0) * (trainer?.ratePerMin ?: 12)
                Text("Estimated cost: ₹$cost", style = MaterialTheme.typography.titleSmall, color = SnoutScoutColors.Primary)
            }

            SSButton(
                text = "Confirm Booking",
                onClick = {
                    scope.launch { snackbarHostState.showSnackbar("Booking confirmed!") }
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedDate != null && selectedTime != null
            )
        }
    }
}
