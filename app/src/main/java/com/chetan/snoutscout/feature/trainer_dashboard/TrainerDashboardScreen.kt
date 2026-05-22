package com.chetan.snoutscout.feature.trainer_dashboard

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
fun TrainerDashboardScreen() {
    val application = LocalContext.current.applicationContext as SnoutScoutApp
    val viewModel: TrainerDashboardViewModel = viewModel(
        factory = TrainerDashboardViewModel.factory(application.appContainer.trainerDashboardRepository)
    )
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val trainer = uiState.trainer

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Trainer dashboard",
            style = MaterialTheme.typography.headlineSmall
        )

        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = trainer?.fullName ?: "Loading profile",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = trainer?.bio ?: "Preparing trainer profile data",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Status: ${trainer?.verificationStatus?.name ?: "PENDING"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Today’s summary",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Upcoming calls, availability, earnings, and report drafts will live here.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        PrimaryButton(
            text = "Manage profile",
            onClick = { }
        )
    }
}