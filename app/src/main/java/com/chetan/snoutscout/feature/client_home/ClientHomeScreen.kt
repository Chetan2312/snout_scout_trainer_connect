package com.chetan.snoutscout.feature.client_home

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chetan.snoutscout.SnoutScoutApp
import com.chetan.snoutscout.core.ui.components.EmptyStateCard
import com.chetan.snoutscout.core.ui.components.TrainerCard

@Composable
fun ClientHomeScreen() {
    val application = LocalContext.current.applicationContext as SnoutScoutApp
    val viewModel: ClientHomeViewModel = viewModel(
        factory = ClientHomeViewModel.factory(application.appContainer.trainerRepository)
    )
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Welcome back",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Featured verified trainers for your next dog-training consult.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        items(uiState.featuredTrainers, key = { it.id }) { trainer ->
            TrainerCard(
                name = trainer.fullName,
                city = trainer.city,
                expertise = trainer.specializations.joinToString(" • ") { it.name.replace("_", " ") },
                ratePerMinute = "₹${trainer.pricePerMinuteInInr}/min",
                rating = "${trainer.rating} ★"
            )
        }

        if (uiState.featuredTrainers.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "No featured trainers yet",
                    subtitle = "Featured trainers will appear here as soon as they are available."
                )
            }
        }

        item {
            EmptyStateCard(
                title = "Your dog profiles will appear here",
                subtitle = "Add your first dog in the Dogs tab to personalize booking and reports."
            )
        }
    }
}