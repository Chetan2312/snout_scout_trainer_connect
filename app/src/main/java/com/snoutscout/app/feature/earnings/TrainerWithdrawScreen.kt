package com.snoutscout.app.feature.earnings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.*

@Composable
fun TrainerWithdrawScreen(onBack: () -> Unit) {
    var method by remember { mutableStateOf("Bank Transfer") }
    var amount by remember { mutableStateOf("") }
    val available = 4200

    Scaffold(topBar = { SSTopBar("Withdraw Earnings", onBack = onBack) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SSCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Available Balance", style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                    Text("₹$available", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
                }
            }

            Text("Withdrawal Method", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Bank Transfer", "UPI").forEach { m ->
                    SSChip(m, method == m, { method = m })
                }
            }

            SSInput(amount, { amount = it }, "Amount (₹)", placeholder = "Enter amount")

            SSCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    InfoRow("Processing time", if (method == "Bank Transfer") "1-2 business days" else "Instant")
                    InfoRow("Minimum withdrawal", "₹500")
                    InfoRow("Platform commission", "20%")
                }
            }

            Spacer(Modifier.weight(1f))
            SSButton(
                text = "Request Withdrawal",
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                enabled = (amount.toIntOrNull() ?: 0) >= 500
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
    }
}
