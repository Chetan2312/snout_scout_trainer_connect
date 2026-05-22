package com.chetan.snoutscout.feature.notifications

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

@Composable
fun NotificationsScreen() {
    val application = LocalContext.current.applicationContext as SnoutScoutApp
    val viewModel: NotificationsViewModel = viewModel(
        factory = NotificationsViewModel.factory(application.appContainer.notificationRepository)
    )
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        items(uiState.items, key = { it.id }) { item ->
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = item.body,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = item.createdAt,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}