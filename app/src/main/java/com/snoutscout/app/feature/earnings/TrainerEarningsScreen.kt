package com.snoutscout.app.feature.earnings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.data.model.MockData

@Composable
fun TrainerEarningsScreen(onBack: () -> Unit, onWithdraw: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            SSTopBar("Earnings", onBack = onBack)
            Box(
                modifier = Modifier.fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(SnoutScoutColors.Secondary, SnoutScoutColors.SecondaryDark)))
                    .padding(20.dp)
            ) {
                Column {
                    Text("Total Earned", color = Color.White.copy(0.7f), style = MaterialTheme.typography.labelMedium)
                    Text("₹89,600", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Column {
                            Text("This Month", color = Color.White.copy(0.7f), style = MaterialTheme.typography.labelSmall)
                            Text("₹12,450", color = Color.White, style = MaterialTheme.typography.titleMedium)
                        }
                        Column {
                            Text("Last Month", color = Color.White.copy(0.7f), style = MaterialTheme.typography.labelSmall)
                            Text("₹15,200", color = Color.White, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
        item {
            SSCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Pending Payout", style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₹4,200", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    }
                    Button(onClick = onWithdraw, shape = RoundedCornerShape(12.dp)) { Text("Withdraw") }
                }
            }
            Spacer(Modifier.height(16.dp))
            SSSectionHeader("Recent Payouts", modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(8.dp))
        }
        items(MockData.PAYOUTS) { payout ->
            ListItem(
                headlineContent = { Text(payout.method) },
                supportingContent = { Text(payout.date) },
                trailingContent = {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("₹${payout.amount}", fontWeight = FontWeight.SemiBold, color = SnoutScoutColors.Success)
                        SSBadge(payout.status, type = when (payout.status) {
                            "completed" -> BadgeType.SUCCESS
                            "pending" -> BadgeType.WARNING
                            else -> BadgeType.ERROR
                        })
                    }
                }
            )
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
        }
    }
}
