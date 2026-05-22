package com.chetan.snoutscout.core.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.chetan.snoutscout.app.AppRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    currentRole: AppRole,
    onRoleToggle: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            FilterChip(
                selected = currentRole == AppRole.TRAINER,
                onClick = onRoleToggle,
                label = {
                    Text(
                        if (currentRole == AppRole.CLIENT) "Client Mode" else "Trainer Mode"
                    )
                },
                colors = FilterChipDefaults.filterChipColors()
            )
        }
    )
}