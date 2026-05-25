package com.snoutscout.app.feature.dog_profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snoutscout.app.SnoutScoutApp
import com.snoutscout.app.core.ui.*

@Composable
fun DogsListScreen(
    onAddDog: () -> Unit,
    onEditDog: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val viewModel: DogViewModel = viewModel(factory = DogViewModelFactory(container))
    val dogs by viewModel.dogs.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            SSTopBar(title = "My Dogs", onBack = onBack, actions = {
                IconButton(onClick = onAddDog) { Icon(Icons.Outlined.Add, "Add Dog") }
            })
        }
    ) { padding ->
        if (dogs.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                SSEmptyState(
                    icon = Icons.Outlined.Pets,
                    title = "No dogs added yet",
                    subtitle = "Add your dog's profile to get personalized training advice",
                    action = { SSButton("Add Dog", onClick = onAddDog) }
                )
            }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 24.dp)) {
                items(dogs) { dog ->
                    SSCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), onClick = { onEditDog(dog.id) }) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("🐕", style = MaterialTheme.typography.headlineMedium)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(dog.name, style = MaterialTheme.typography.titleMedium)
                                Text("${dog.breed} · ${dog.age} · ${dog.gender}", style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    SSBadge(
                                        dog.vaccination,
                                        type = when (dog.vaccination) {
                                            "Up to date" -> BadgeType.SUCCESS
                                            "Overdue" -> BadgeType.WARNING
                                            else -> BadgeType.DEFAULT
                                        }
                                    )
                                    if (dog.issues.isNotEmpty()) {
                                        SSBadge("${dog.issues.size} issues", type = BadgeType.PRIMARY)
                                    }
                                }
                            }
                            Icon(Icons.Outlined.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
