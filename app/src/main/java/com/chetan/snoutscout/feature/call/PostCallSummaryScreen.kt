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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chetan.snoutscout.SnoutScoutApp
import com.chetan.snoutscout.core.ui.components.PrimaryButton

@Composable
fun PostCallSummaryScreen(
    onDone: () -> Unit
) {
    val application = LocalContext.current.applicationContext as SnoutScoutApp
    val viewModel: CallViewModel = viewModel(
        factory = CallViewModel.factory(application.appContainer.callRepository)
    )
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Call summary",
            style = MaterialTheme.typography.headlineSmall
        )

        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Trainer: ${uiState.trainerName}", style = MaterialTheme.typography.titleMedium)
                Text("Dog: ${uiState.dogName}", style = MaterialTheme.typography.bodyMedium)
                Text("Duration: ${formatElapsed(uiState.elapsedSeconds)}", style = MaterialTheme.typography.bodyMedium)
                Text("Total billed: ₹${uiState.deductedAmount}", style = MaterialTheme.typography.bodyMedium)
                Text("Call type: ${uiState.callType.name}", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Next steps", style = MaterialTheme.typography.titleMedium)
                Text(
                    "AI notes and trainer-approved recommendations will appear in your reports section shortly.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        PrimaryButton(
            text = "Done",
            onClick = onDone
        )
    }
}