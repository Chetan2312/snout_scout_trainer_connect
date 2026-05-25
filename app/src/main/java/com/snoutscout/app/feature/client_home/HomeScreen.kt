package com.snoutscout.app.feature.client_home

import androidx.compose.ui.draw.alpha
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snoutscout.app.SnoutScoutApp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.core.util.CurrencyFormatter
import com.snoutscout.app.data.model.MockData
import com.snoutscout.app.data.model.TrainerProfile

@Composable
fun HomeScreen(
    walletBalance: Int,
    userName: String,
    onNavigateToTrainer: (String) -> Unit,
    onNavigateToBrowse: () -> Unit,
    onNavigateToDogs: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(container))
    val topTrainers by viewModel.topTrainers.collectAsStateWithLifecycle()
    val recentSession = MockData.SESSIONS.firstOrNull()

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(SnoutScoutColors.Primary.copy(0.12f)),
                    contentAlignment = Alignment.Center
                ) { Text("🐾", fontSize = 20.sp) }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
                    val greeting = when {
                        hour < 12 -> "Good morning"
                        hour < 17 -> "Good afternoon"
                        else -> "Good evening"
                    }
                    Text("$greeting, ${userName.split(" ").first()} 👋", style = MaterialTheme.typography.titleMedium)
                }
                BadgedBox(badge = { Badge { Text("2") } }) {
                    IconButton(onClick = onNavigateToNotifications) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                    }
                }
            }
        }

        item {
            // Quick actions
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Triple("Instant Call", Icons.Outlined.Phone, onNavigateToBrowse),
                    Triple("Schedule", Icons.Outlined.CalendarMonth, onNavigateToBrowse),
                    Triple("My Dogs", Icons.Outlined.Pets, onNavigateToDogs),
                    Triple("Wallet", Icons.Outlined.AccountBalanceWallet, onNavigateToWallet)
                ).forEach { (label, icon, action) ->
                    Column(
                        modifier = Modifier.weight(1f).clickable { action() },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(48.dp).clip(CircleShape).background(SnoutScoutColors.Primary.copy(0.1f)),
                            contentAlignment = Alignment.Center
                        ) { Icon(icon, contentDescription = label, tint = SnoutScoutColors.Primary) }
                        Spacer(Modifier.height(4.dp))
                        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        item {
            // Wallet balance card
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(SnoutScoutColors.Primary, SnoutScoutColors.PrimaryDark)))
                    .clickable { onNavigateToWallet() }
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Wallet Balance", color = Color.White.copy(0.7f), style = MaterialTheme.typography.labelMedium)
                        Text(CurrencyFormatter.format(walletBalance), color = Color.White,
                            fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                        Text("≈ ${walletBalance / 12} min at avg. rate", color = Color.White.copy(0.7f),
                            style = MaterialTheme.typography.labelSmall)
                    }
                    OutlinedButton(
                        onClick = onNavigateToWallet,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(0.5f)),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("+ Recharge") }
                }
            }
        }

        if (recentSession != null) {
            item {
                Spacer(Modifier.height(8.dp))
                SSSectionHeader("Recent Session", modifier = Modifier.padding(horizontal = 16.dp),
                    action = "View All", onAction = onNavigateToHistory)
                Spacer(Modifier.height(8.dp))
                SSCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                        SSAvatar(initials = recentSession.trainerName.split(" ").take(2).joinToString("") { it.first().toString() })
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(recentSession.trainerName, style = MaterialTheme.typography.titleSmall)
                                Spacer(Modifier.width(8.dp))
                                if (recentSession.hasReport) SSBadge("Report ready", type = BadgeType.SUCCESS)
                            }
                            Text(recentSession.dogName, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(4.dp))
                            Text(recentSession.summary, style = MaterialTheme.typography.bodySmall,
                                maxLines = 2, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
            SSSectionHeader("Top-Rated Trainers", modifier = Modifier.padding(horizontal = 16.dp),
                action = "See All", onAction = onNavigateToBrowse)
            Spacer(Modifier.height(8.dp))
        }

        items(topTrainers.take(5)) { trainer ->
            SSTrainerCard(
                trainer = trainer,
                onClick = { onNavigateToTrainer(trainer.id) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        item {
            Spacer(Modifier.height(16.dp))
            SSSectionHeader("Coming Soon", modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(8.dp))
            ComingSoonGrid()
        }
    }
}

@Composable
private fun ComingSoonGrid() {
    val items = listOf(
        Pair("🤖", "AI Behavior\nAssistant"),
        Pair("👥", "Group\nConsultations"),
        Pair("🎓", "Training\nCourses"),
        Pair("🏥", "Vet\nIntegration")
    )
    Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { (emoji, label) ->
                    SSCard(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier.padding(16.dp).fillMaxWidth().alpha(0.7f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(emoji, fontSize = 28.sp)
                            Spacer(Modifier.height(4.dp))
                            Icon(Icons.Outlined.Lock, contentDescription = null, modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(2.dp))
                            Text(label, style = MaterialTheme.typography.labelSmall, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                            Text("Coming soon", style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

