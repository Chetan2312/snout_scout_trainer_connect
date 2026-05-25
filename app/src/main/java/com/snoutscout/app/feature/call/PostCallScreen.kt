package com.snoutscout.app.feature.call

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.core.util.DateFormatter
import com.snoutscout.app.data.model.MockData

@Composable
fun PostCallScreen(
    trainerId: String,
    elapsedSeconds: Int,
    cost: Int,
    onHome: () -> Unit,
    onHistory: () -> Unit
) {
    val trainer = remember { MockData.TRAINERS.find { it.id == trainerId } }
    var rating by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))
        Box(
            modifier = Modifier.size(72.dp).clip(CircleShape).background(SnoutScoutColors.Success.copy(0.15f)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Outlined.Check, null, tint = SnoutScoutColors.Success, modifier = Modifier.size(40.dp)) }
        Spacer(Modifier.height(16.dp))
        Text("Session Complete", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Your consultation has ended", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))

        SSCard(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                trainer?.let { t ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SSAvatar(initials = t.name.split(" ").take(2).joinToString("") { it.first().toString() })
                        Spacer(Modifier.width(12.dp))
                        Text(t.name, style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(Modifier.height(16.dp))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    StatItem(DateFormatter.formatElapsed(elapsedSeconds), "Duration")
                    VerticalDivider(modifier = Modifier.height(40.dp))
                    StatItem("₹$cost", "Total Cost")
                    VerticalDivider(modifier = Modifier.height(40.dp))
                    StatItem("Voice", "Type")
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        SSCard(modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Description, null, tint = SnoutScoutColors.Primary, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Text("AI-generated report will be ready within 30 minutes",
                    style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("How was your session?", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..5).forEach { star ->
                IconButton(onClick = { rating = star }) {
                    Icon(
                        if (star <= rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = "$star stars",
                        tint = if (star <= rating) SnoutScoutColors.Accent else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))
        SSButton("Back to Home", onClick = onHome, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        SSButton("View Session History", onClick = onHistory, modifier = Modifier.fillMaxWidth(), variant = ButtonVariant.SECONDARY)
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
