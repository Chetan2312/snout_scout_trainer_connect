package com.snoutscout.app.feature.trainer_browse

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.snoutscout.app.core.ui.SSButton
import com.snoutscout.app.core.ui.SSChip
import com.snoutscout.app.core.ui.ButtonVariant
import com.snoutscout.app.data.model.MockData

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    filters: TrainerFilters,
    onDismiss: () -> Unit,
    onApply: () -> Unit,
    viewModel: BrowseTrainersViewModel,
    resultCount: Int
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp).padding(bottom = 40.dp)
        ) {
            Text("Filter Trainers", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            Text("City", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SSChip("All Cities", filters.city == "All Cities", { viewModel.updateCity("All Cities") })
                MockData.ALL_CITIES.forEach { city ->
                    SSChip(city, filters.city == city, { viewModel.updateCity(city) })
                }
            }
            Spacer(Modifier.height(16.dp))

            Text("Specialization", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SSChip("Any", filters.specialization == "Any", { viewModel.updateSpec("Any") })
                MockData.ALL_SPECIALIZATIONS.take(8).forEach { spec ->
                    SSChip(spec, filters.specialization == spec, { viewModel.updateSpec(spec) })
                }
            }
            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Online now only", style = MaterialTheme.typography.titleSmall)
                    Text("Accepting calls", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = filters.onlineOnly, onCheckedChange = viewModel::updateOnlineOnly)
            }
            Spacer(Modifier.height(16.dp))

            Text("Max price: ₹${filters.maxPrice}/min", style = MaterialTheme.typography.titleSmall)
            Slider(
                value = filters.maxPrice.toFloat(),
                onValueChange = { viewModel.updateMaxPrice(it.toInt()) },
                valueRange = 5f..25f,
                steps = 19
            )
            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SSButton("Reset", onClick = { viewModel.resetFilters() }, modifier = Modifier.weight(1f), variant = ButtonVariant.SECONDARY)
                SSButton("Show $resultCount trainers", onClick = onApply, modifier = Modifier.weight(1f))
            }
        }
    }
}
