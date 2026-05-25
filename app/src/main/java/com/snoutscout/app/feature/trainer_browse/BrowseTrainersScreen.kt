package com.snoutscout.app.feature.trainer_browse

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snoutscout.app.SnoutScoutApp
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.data.model.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseTrainersScreen(
    onTrainerClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val viewModel: BrowseTrainersViewModel = viewModel(factory = BrowseTrainersViewModelFactory(container))
    val trainers by viewModel.trainers.collectAsStateWithLifecycle()
    val filters by viewModel.filters.collectAsStateWithLifecycle()
    var showFilterSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SSTopBar(
                title = "Find a Trainer",
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(Icons.Outlined.FilterList, "Filters")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                OutlinedTextField(
                    value = filters.search,
                    onValueChange = viewModel::updateSearch,
                    placeholder = { Text("Search by name, specialization...") },
                    leadingIcon = { Icon(Icons.Outlined.Search, null) },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    singleLine = true,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { SSChip("Top Rated", filters.sortBy == SortBy.RATING, { viewModel.updateSort(SortBy.RATING) }) }
                    item { SSChip("Lowest Price", filters.sortBy == SortBy.PRICE, { viewModel.updateSort(SortBy.PRICE) }) }
                    item { SSChip("Most Experienced", filters.sortBy == SortBy.EXPERIENCE, { viewModel.updateSort(SortBy.EXPERIENCE) }) }
                }
                Spacer(Modifier.height(8.dp))
            }
            item {
                Text(
                    "${trainers.size} trainers found",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
            if (trainers.isEmpty()) {
                item {
                    SSEmptyState(
                        icon = Icons.Outlined.Search,
                        title = "No trainers found",
                        subtitle = "Try adjusting your filters",
                        modifier = Modifier.fillMaxWidth().padding(top = 64.dp)
                    )
                }
            } else {
                items(trainers) { trainer ->
                    SSTrainerCard(
                        trainer = trainer,
                        onClick = { onTrainerClick(trainer.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterBottomSheet(
            filters = filters,
            onDismiss = { showFilterSheet = false },
            onApply = { showFilterSheet = false },
            viewModel = viewModel,
            resultCount = trainers.size
        )
    }
}
