package com.snoutscout.app.feature.trainer_dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.core.util.DateFormatter
import com.snoutscout.app.data.model.CallType
import com.snoutscout.app.data.model.MockData

@Composable
fun TrainerDashboardScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToAvailability: () -> Unit,
    onNavigateToNotes: () -> Unit,
    onNavigateToEarnings: () -> Unit
) {
    val trainer = MockData.TRAINERS.first()

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Trainer Dashboard", style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(trainer.name, style = MaterialTheme.typography.headlineSmall)
                }
                IconButton(onClick = {}) { Icon(Icons.Outlined.Notifications, null) }
            }
        }

        item {
            // Stats grid
            Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard("This Month", "₹12,450", Icons.Outlined.TrendingUp, SnoutScoutColors.Success, Modifier.weight(1f))
                    StatCard("Pending", "₹4,200", Icons.Outlined.AccountBalanceWallet, SnoutScoutColors.Secondary, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard("Sessions", "23", Icons.Outlined.Phone, SnoutScoutColors.Primary, Modifier.weight(1f))
                    StatCard("Avg Rating", "4.9★", Icons.Outlined.Star, SnoutScoutColors.Accent, Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            SSSectionHeader("Upcoming Calls", modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(8.dp))
        }

        items(MockData.UPCOMING_CALLS) { call ->
            SSCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SSAvatar(initials = call.clientName.take(2), size = 40)
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(call.clientName, style = MaterialTheme.typography.titleSmall)
                            Text("${call.dogName} · ${call.breed}", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        SSBadge(call.type.name, type = if (call.type == CallType.VIDEO) BadgeType.PRIMARY else BadgeType.DEFAULT)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row {
                        Text(DateFormatter.formatDateTime(call.scheduledAt), style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(" · ${call.durationMinutes} min", style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("📋 ${call.issue}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
            SSSectionHeader("Quick Actions", modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Triple("My Profile", Icons.Outlined.Person, onNavigateToProfile),
                    Triple("Availability", Icons.Outlined.CalendarMonth, onNavigateToAvailability),
                    Triple("Notes", Icons.Outlined.Description, onNavigateToNotes),
                    Triple("Earnings", Icons.Outlined.AttachMoney, onNavigateToEarnings)
                ).forEach { (label, icon, action) ->
                    Column(
                        modifier = Modifier.weight(1f).clickable { action() },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(48.dp).clip(CircleShape).background(SnoutScoutColors.Primary.copy(0.1f)),
                            contentAlignment = Alignment.Center
                        ) { Icon(icon, null, tint = SnoutScoutColors.Primary) }
                        Spacer(Modifier.height(4.dp))
                        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            SSCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Shield, null, tint = SnoutScoutColors.Primary, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Verified Trainer", style = MaterialTheme.typography.titleSmall)
                        Text("KYC complete", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    SSBadge("Active", type = BadgeType.SUCCESS)
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, icon: ImageVector, tint: Color, modifier: Modifier = Modifier) {
    SSCard(modifier = modifier) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(tint.copy(0.15f)),
                    contentAlignment = Alignment.Center
                ) { Icon(icon, null, tint = tint, modifier = Modifier.size(18.dp)) }
            }
            Spacer(Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
