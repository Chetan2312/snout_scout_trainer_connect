package com.chetan.snoutscout.feature.dog_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.chetan.snoutscout.core.ui.components.EmptyStateCard
import com.chetan.snoutscout.core.ui.components.PrimaryButton

@Composable
fun DogProfilesScreen() {
    val application = LocalContext.current.applicationContext as SnoutScoutApp
    val viewModel: DogProfileViewModel = viewModel(
        factory = DogProfileViewModel.factory(application.appContainer.dogRepository)
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
                    text = "Dog profiles",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Manage behavior history, vaccination notes, and previous session summaries.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        items(uiState.dogs, key = { it.id }) { dog ->
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = dog.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${dog.breed} • ${dog.ageInMonths} months",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Vaccination: ${dog.vaccinationStatus}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Behavior: ${dog.behavioralIssues}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        if (uiState.dogs.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "No dogs added yet",
                    subtitle = "Create a dog profile with breed, age, vaccination, and behavior notes."
                )
            }
        }

        item {
            PrimaryButton(
                text = "Add dog profile",
                onClick = { }
            )
        }
    }
}