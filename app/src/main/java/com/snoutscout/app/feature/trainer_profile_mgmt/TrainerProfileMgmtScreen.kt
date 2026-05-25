package com.snoutscout.app.feature.trainer_profile_mgmt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.data.model.MockData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TrainerProfileMgmtScreen(onBack: () -> Unit) {
    val trainer = MockData.TRAINERS.first()
    var bio by remember { mutableStateOf(trainer.bio) }
    var rate by remember { mutableStateOf(trainer.ratePerMin.toString()) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { SSTopBar("My Profile", onBack = onBack) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SSAvatar(initials = trainer.name.split(" ").take(2).joinToString("") { it.first().toString() }, size = 72)
                Spacer(Modifier.width(16.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(trainer.name, style = MaterialTheme.typography.headlineSmall)
                        if (trainer.isVerified) {
                            Spacer(Modifier.width(6.dp))
                            Icon(Icons.Outlined.Verified, "Verified", tint = SnoutScoutColors.Primary, modifier = Modifier.size(20.dp))
                        }
                    }
                    SSBadge("Verified Trainer", type = BadgeType.SUCCESS)
                }
            }

            OutlinedTextField(
                value = bio, onValueChange = { bio = it },
                label = { Text("Bio") }, modifier = Modifier.fillMaxWidth(),
                minLines = 3, shape = RoundedCornerShape(12.dp)
            )

            SSInput(rate, { rate = it }, "Rate per minute (₹)", placeholder = "15")

            Text("Specializations", style = MaterialTheme.typography.titleSmall)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                trainer.specializations.forEach { spec ->
                    SSBadge(spec, type = BadgeType.PRIMARY)
                    Spacer(Modifier.width(4.dp))
                }
            }

            Text("Languages", style = MaterialTheme.typography.titleSmall)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                trainer.languages.forEach { lang ->
                    SSBadge(lang)
                    Spacer(Modifier.width(4.dp))
                }
            }

            Text("Certifications", style = MaterialTheme.typography.titleSmall)
            trainer.certifications.forEach { cert ->
                Text("• $cert", style = MaterialTheme.typography.bodyMedium)
            }

            SSButton(
                text = "Save Changes",
                onClick = { scope.launch { snackbar.showSnackbar("Profile updated!") } },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
