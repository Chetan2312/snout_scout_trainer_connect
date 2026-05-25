package com.snoutscout.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snoutscout.app.core.theme.SnoutScoutColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onComplete: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2200)
        onComplete()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(SnoutScoutColors.Primary, SnoutScoutColors.PrimaryDark),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🐾", fontSize = 56.sp)
            }
            Spacer(Modifier.height(24.dp))
            Text(
                "Snout Scout",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Trusted dog trainers, one call away",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp
            )
        }
    }
}
