package com.chetan.snoutscout.feature.trainer_browse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
import com.chetan.snoutscout.domain.model.TrainerFilter

@Composable
fun BrowseTrainersScreen() {
    val application = LocalContext.current.applicationContext as SnoutScoutApp
    val viewModel: BrowseTrainersViewModel = viewModel(
        factory = BrowseTrainersViewModel.factory(application.appContainer.trainerRepository)
    )
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Browse trainers across India",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Search premium dog trainers by city, online availability, specialization, and budget.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = {
                        viewModel.applyFilter(
                            TrainerFilter(
                                city = "Pune",
                                puppyTrainer = true
                            )
                        )
                    },
                    label = { Text("Pune + Puppy") },
                    colors = AssistChipDefaults.assistChipColors()
                )
                AssistChip(
                    onClick = {
                        viewModel.applyFilter(
                            TrainerFilter(
                                onlineAvailability = true,
                                maxPricePerMinute = 35
                            )
                        )
                    },
                    label = { Text("Online ≤ ₹35") },
                    colors = AssistChipDefaults.assistChipColors()
                )
                AssistChip(
                    onClick = { viewModel.loadAll() },
                    label = { Text("Clear") },
                    colors = AssistChipDefaults.assistChipColors()
                )
            }
        }

        items(uiState.trainers, key = { it.id }) { trainer ->
            TrainerCard(
                name = trainer.fullName,
                city = trainer.city,
                expertise = trainer.specializations.joinToString(" • ") { it.name.replace("_", " ") },
                ratePerMinute = "₹${trainer.pricePerMinuteInInr}/min",
                rating = "${trainer.rating} ★"
            )
        }

        if (uiState.trainers.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "No trainers match this filter",
                    subtitle = "Try clearing filters or broadening the city and pricing range."
                )
            }
        }
    }
}