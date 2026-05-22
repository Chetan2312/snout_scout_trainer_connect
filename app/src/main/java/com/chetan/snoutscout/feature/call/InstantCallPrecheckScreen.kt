package com.chetan.snoutscout.feature.call

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chetan.snoutscout.SnoutScoutApp
import com.chetan.snoutscout.core.ui.components.PrimaryButton

@Composable
fun InstantCallPrecheckScreen(
    onCallStarted: () -> Unit
) {
    val application = LocalContext.current.applicationContext as SnoutScoutApp
    val viewModel: CallViewModel = viewModel(
        factory = CallViewModel.factory(application.appContainer.callRepository)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Instant consultation pre-check",
            style = MaterialTheme.typography.headlineSmall
        )

        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Trainer: Aarav Kulkarni", style = MaterialTheme.typography.titleMedium)
                Text("Dog: Bruno", style = MaterialTheme.typography.bodyMedium)
                Text("Wallet ready: ₹1200", style = MaterialTheme.typography.bodyMedium)
                Text("Voice from ₹25/min • Emergency from ₹45/min", style = MaterialTheme.typography.bodySmall)
            }
        }

        PrimaryButton(
            text = "Start voice call",
            onClick = {
                viewModel.startVoiceCall()
                onCallStarted()
            }
        )
        PrimaryButton(
            text = "Start video call",
            onClick = {
                viewModel.startVideoCall()
                onCallStarted()
            }
        )
        PrimaryButton(
            text = "Emergency quick call",
            onClick = {
                viewModel.startEmergencyCall()
                onCallStarted()
            }
        )
    }
}