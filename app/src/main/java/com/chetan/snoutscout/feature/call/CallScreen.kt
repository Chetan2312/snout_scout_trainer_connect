package com.chetan.snoutscout.feature.call

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chetan.snoutscout.SnoutScoutApp
import com.chetan.snoutscout.core.ui.components.PrimaryButton

@Composable
fun CallScreen(
    onCallEnded: () -> Unit
) {
    val application = LocalContext.current.applicationContext as SnoutScoutApp
    val viewModel: CallViewModel = viewModel(
        factory = CallViewModel.factory(application.appContainer.callRepository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.lowBalanceWarningVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissLowBalanceWarning() },
            confirmButton = {
                PrimaryButton(
                    text = "Recharge now",
                    onClick = { viewModel.dismissLowBalanceWarning() }
                )
            },
            dismissButton = {
                PrimaryButton(
                    text = "Continue",
                    onClick = { viewModel.dismissLowBalanceWarning() }
                )
            },
            title = { Text("Low balance warning") },
            text = {
                Text("Your wallet balance is getting low. Recharge now to avoid call interruption.")
            }
        )
    }

    if (uiState.callState.name == "ENDED") {
        onCallEnded()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (uiState.isEmergency) "Emergency consultation" else "Live consultation",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(uiState.trainerName, style = MaterialTheme.typography.titleLarge)
                Text("For ${uiState.dogName}", style = MaterialTheme.typography.bodyMedium)
                Text("State: ${uiState.callState.name}", style = MaterialTheme.typography.bodyMedium)
                Text("Timer: ${formatElapsed(uiState.elapsedSeconds)}", style = MaterialTheme.typography.headlineMedium)
                Text("Rate: ₹${uiState.pricePerMinuteInInr}/min", style = MaterialTheme.typography.bodyMedium)
                Text("Deducted: ₹${uiState.deductedAmount}", style = MaterialTheme.typography.bodyMedium)
                Text("Remaining: ₹${uiState.remainingBalance}", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PrimaryButton(
                text = if (uiState.isMuted) "Unmute" else "Mute",
                onClick = { viewModel.toggleMute() },
                modifier = Modifier.weight(1f)
            )
            PrimaryButton(
                text = if (uiState.isSpeakerOn) "Speaker on" else "Speaker off",
                onClick = { viewModel.toggleSpeaker() },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PrimaryButton(
                text = if (uiState.callState.name == "PAUSED") "Resume" else "Pause",
                onClick = { viewModel.pauseOrResume() },
                modifier = Modifier.weight(1f)
            )
            PrimaryButton(
                text = if (uiState.isVideoEnabled) "Video on" else "Video off",
                onClick = { viewModel.toggleVideo() },
                modifier = Modifier.weight(1f)
            )
        }

        PrimaryButton(
            text = "End call",
            onClick = { viewModel.endCall() }
        )
    }
}