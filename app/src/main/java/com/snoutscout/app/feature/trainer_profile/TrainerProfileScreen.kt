package com.snoutscout.app.feature.trainer_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snoutscout.app.SnoutScoutApp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.data.model.MockData
import com.snoutscout.app.data.model.TrainerProfile

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TrainerProfileScreen(
    trainerId: String,
    walletBalance: Int,
    onBack: () -> Unit,
    onCallNow: (String, String) -> Unit,
    onSchedule: (String) -> Unit
) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val viewModel: TrainerProfileViewModel = viewModel(factory = TrainerProfileViewModelFactory(container))
    val trainer by viewModel.trainer.collectAsStateWithLifecycle()

    LaunchedEffect(trainerId) { viewModel.load(trainerId) }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("About", "Reviews", "Credentials")

    trainer?.let { t ->
        Scaffold(
            topBar = {
                SSTopBar(title = "", onBack = onBack, actions = {
                    IconButton(onClick = {}) { Icon(Icons.Outlined.Share, "Share") }
                })
            }
        ) { padding ->
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).background(MaterialTheme.colorScheme.background)) {
                item {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        SSAvatar(
                            initials = t.name.split(" ").take(2).joinToString("") { it.first().toString() },
                            size = 72
                        )
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(t.name, style = MaterialTheme.typography.headlineSmall)
                            if (t.isVerified) {
                                Spacer(Modifier.width(6.dp))
                                Icon(Icons.Outlined.Verified, "Verified", tint = SnoutScoutColors.Primary, modifier = Modifier.size(20.dp))
                            }
                        }
                        Text("${t.city} · ${t.experience} years exp", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            StatItem("${t.rating}", "Rating")
                            VerticalDivider(modifier = Modifier.height(40.dp))
                            StatItem("${t.totalSessions}", "Sessions")
                            VerticalDivider(modifier = Modifier.height(40.dp))
                            StatItem("₹${t.ratePerMin}/min", "Rate")
                        }
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { onCallNow(t.id, "VOICE") },
                                enabled = t.isOnline,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Outlined.Phone, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Call Now")
                            }
                            Button(
                                onClick = { onCallNow(t.id, "VIDEO") },
                                enabled = t.isOnline,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SnoutScoutColors.Secondary)
                            ) {
                                Icon(Icons.Outlined.Videocam, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Video")
                            }
                            OutlinedButton(
                                onClick = { onSchedule(t.id) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) { Text("Schedule") }
                        }
                    }
                }

                item {
                    TabRow(selectedTabIndex = selectedTab) {
                        tabs.forEachIndexed { idx, title ->
                            Tab(selected = selectedTab == idx, onClick = { selectedTab = idx }, text = { Text(title) })
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                when (selectedTab) {
                    0 -> item { AboutTab(t) }
                    1 -> {
                        item {
                            val reviews = MockData.REVIEWS.filter { it.trainerId == t.id }
                            if (reviews.isEmpty()) {
                                Text("No reviews yet", modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        items(MockData.REVIEWS.filter { it.trainerId == t.id }) { review ->
                            SSCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                                Column(Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        SSAvatar(initials = review.userName.take(2), size = 36)
                                        Spacer(Modifier.width(10.dp))
                                        Column {
                                            Text(review.userName, style = MaterialTheme.typography.titleSmall)
                                            SSRating(review.rating.toFloat())
                                        }
                                        Spacer(Modifier.weight(1f))
                                        Text(review.date, style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Text(review.text, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                    2 -> item { CredentialsTab(t) }
                }
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun AboutTab(t: TrainerProfile) {
    Column(Modifier.padding(16.dp)) {
        Text(t.bio, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        Text("Specializations", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            t.specializations.forEach { spec ->
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(999.dp))
                        .background(SnoutScoutColors.Primary.copy(0.12f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) { Text(spec, style = MaterialTheme.typography.labelSmall, color = SnoutScoutColors.Primary) }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Languages", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            t.languages.forEach { lang ->
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(999.dp))
                        .background(SnoutScoutColors.Accent.copy(0.12f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) { Text(lang, style = MaterialTheme.typography.labelSmall, color = SnoutScoutColors.Secondary) }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Breed Expertise", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            t.breeds.forEach { breed ->
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(999.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) { Text(breed, style = MaterialTheme.typography.labelSmall) }
            }
        }
    }
}

@Composable
private fun CredentialsTab(t: TrainerProfile) {
    Column(Modifier.padding(16.dp)) {
        t.certifications.forEach { cert ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                Icon(Icons.Outlined.EmojiEvents, null, tint = SnoutScoutColors.Accent, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text(cert, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(Modifier.height(16.dp))
        SSCard {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Shield, null, tint = SnoutScoutColors.Primary, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Identity Verified", style = MaterialTheme.typography.titleSmall)
                    Text("KYC and certification documents verified by Snout Scout", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
