package com.snoutscout.app.feature.call

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snoutscout.app.SnoutScoutApp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.core.util.CurrencyFormatter
import com.snoutscout.app.data.model.DogProfile
import com.snoutscout.app.data.model.MockData
import com.snoutscout.app.feature.dog_profile.DogViewModel
import com.snoutscout.app.feature.dog_profile.DogViewModelFactory
import com.snoutscout.app.feature.trainer_profile.TrainerProfileViewModel
import com.snoutscout.app.feature.trainer_profile.TrainerProfileViewModelFactory
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CallPreCheckScreen(
    trainerId: String,
    callType: String,
    walletBalance: Int,
    onBack: () -> Unit,
    onStartCall: (String, String, String) -> Unit,
    onRecharge: () -> Unit
) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val trainerVM: TrainerProfileViewModel = viewModel(factory = TrainerProfileViewModelFactory(container))
    val dogVM: DogViewModel = viewModel(factory = DogViewModelFactory(container))
    val trainer by trainerVM.trainer.collectAsStateWithLifecycle()
    val dogs by dogVM.dogs.collectAsStateWithLifecycle()

    LaunchedEffect(trainerId) { trainerVM.load(trainerId) }

    var selectedDogId by remember { mutableStateOf<String?>(null) }
    var consentGiven by remember { mutableStateOf(false) }

    val ratePerMin = trainer?.ratePerMin ?: 12
    val estimatedMinutes = walletBalance / ratePerMin
    val lowBalance = estimatedMinutes < 5

    Scaffold(topBar = { SSTopBar("Start ${callType.lowercase().replaceFirstChar { it.uppercase() }} Call", onBack = onBack) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            trainer?.let { t ->
                SSCard {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        SSAvatar(initials = t.name.split(" ").take(2).joinToString("") { it.first().toString() })
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(t.name, style = MaterialTheme.typography.titleMedium)
                            Text("${t.city} · ₹${t.ratePerMin}/min", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        SSBadge(if (t.isOnline) "Online" else "Offline",
                            type = if (t.isOnline) BadgeType.SUCCESS else BadgeType.DEFAULT)
                    }
                }
            }

            SSCard(modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text("Your Balance", style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(CurrencyFormatter.format(walletBalance), style = MaterialTheme.typography.titleLarge)
                    }
                    Column(Modifier.weight(1f)) {
                        Text("Estimated Time", style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("≈ $estimatedMinutes min", style = MaterialTheme.typography.titleLarge,
                            color = if (lowBalance) SnoutScoutColors.Error else MaterialTheme.colorScheme.onSurface)
                    }
                }
                if (lowBalance) {
                    Column(Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)) {
                        Text("Low balance — Recharge now", color = SnoutScoutColors.Error,
                            style = MaterialTheme.typography.labelMedium)
                        Spacer(Modifier.height(8.dp))
                        SSButton("Recharge Now", onClick = onRecharge, modifier = Modifier.fillMaxWidth(), variant = ButtonVariant.SECONDARY)
                    }
                }
            }

            Text("Select Dog", style = MaterialTheme.typography.titleMedium)
            dogs.forEach { dog ->
                SSCard(modifier = Modifier.fillMaxWidth(), onClick = { selectedDogId = dog.id }) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = selectedDogId == dog.id, onClick = { selectedDogId = dog.id })
                        Spacer(Modifier.width(8.dp))
                        Text("🐕", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f)) {
                            Text(dog.name, style = MaterialTheme.typography.titleSmall)
                            Text("${dog.breed} · ${dog.age}", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        if (selectedDogId == dog.id) {
                            Icon(Icons.Outlined.CheckCircle, null, tint = SnoutScoutColors.Primary)
                        }
                    }
                }
            }

            SSCard {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Checkbox(checked = consentGiven, onCheckedChange = { consentGiven = it })
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "I consent to call recording and AI-generated session notes for training improvement purposes.",
                        style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f)
                    )
                }
            }

            SSButton(
                text = "Start ${callType.lowercase().replaceFirstChar { it.uppercase() }} Call",
                onClick = { selectedDogId?.let { dogId -> onStartCall(trainerId, dogId, callType) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedDogId != null && consentGiven
            )
        }
    }
}
