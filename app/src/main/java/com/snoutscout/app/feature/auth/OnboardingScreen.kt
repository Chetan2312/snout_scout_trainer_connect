package com.snoutscout.app.feature.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snoutscout.app.R
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.SSButton
import kotlinx.coroutines.launch

private data class OnboardingPage(val emoji: String, val icon: ImageVector, val title: String, val subtitle: String)

private val pages = listOf(
    OnboardingPage("🔍", Icons.Outlined.Search, "Find Expert Trainers",
        "Browse verified dog trainers across India — filtered by specialization, language, and city."),
    OnboardingPage("📞", Icons.Outlined.Phone, "Connect Instantly",
        "Start a voice or video consultation in under 2 minutes. Pay only for the time you use."),
    OnboardingPage("📄", Icons.Outlined.Description, "Get Detailed Reports",
        "Receive AI-powered session reports with training plans, routines, and follow-up advice.")
)

@Composable
fun OnboardingScreen(onGetStarted: () -> Unit) {
    val pagerState = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            val p = pages[page]
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(SnoutScoutColors.Primary.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (page == 0) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "Snout Scout Logo",
                            modifier = Modifier.size(100.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(p.emoji, fontSize = 48.sp)
                    }
                }
                Spacer(Modifier.height(32.dp))
                Text(p.title, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Text(p.subtitle, style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            }
        }

        // Dot indicators
        Row(
            modifier = Modifier.padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pages.size) { idx ->
                val selected = pagerState.currentPage == idx
                val width = if (selected) 24.dp else 8.dp
                val color by animateColorAsState(
                    if (selected) SnoutScoutColors.Primary else SnoutScoutColors.Border, label = "dot"
                )
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(width)
                        .clip(RoundedCornerShape(999.dp))
                        .background(color)
                )
            }
        }

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (pagerState.currentPage > 0) {
                OutlinedButton(
                    onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Back") }
            } else {
                Spacer(Modifier.weight(1f))
            }
            SSButton(
                text = if (pagerState.currentPage == pages.size - 1) "Get Started" else "Next",
                onClick = {
                    if (pagerState.currentPage == pages.size - 1) onGetStarted()
                    else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
