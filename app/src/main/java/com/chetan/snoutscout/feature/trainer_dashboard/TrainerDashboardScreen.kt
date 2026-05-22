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
import androidx.compose.ui.unit.dp
import com.chetan.snoutscout.core.ui.components.PrimaryButton

@Composable
fun TrainerDashboardScreen() {
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
                    text = "Verification status",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "KYC under review • Featured plan active",
                    style = MaterialTheme.typography.bodyMedium
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
                    text = "3 upcoming consultations • ₹2,450 projected earnings",
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