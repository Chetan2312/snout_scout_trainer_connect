package com.chetan.snoutscout.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chetan.snoutscout.core.ui.components.PrimaryButton

@Composable
fun OnboardingScreen(
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(
            text = "Premium dog training support, built for Indian pet parents.",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Browse verified trainers, book instant consultations, manage your dog profiles, and receive structured post-session guidance.",
            style = MaterialTheme.typography.bodyLarge
        )
        PrimaryButton(
            text = "Get started",
            onClick = onContinue
        )
    }
}