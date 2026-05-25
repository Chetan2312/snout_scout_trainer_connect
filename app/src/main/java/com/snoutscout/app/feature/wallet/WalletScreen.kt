package com.snoutscout.app.feature.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snoutscout.app.SnoutScoutApp
import com.snoutscout.app.core.theme.SnoutScoutColors
import com.snoutscout.app.core.ui.*
import com.snoutscout.app.core.util.CurrencyFormatter
import com.snoutscout.app.core.util.DateFormatter
import com.snoutscout.app.data.model.MockData
import com.snoutscout.app.data.model.TransactionType
import androidx.compose.ui.window.Dialog

@Composable
fun WalletScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val container = (context.applicationContext as SnoutScoutApp).container
    val viewModel: WalletViewModel = viewModel(factory = WalletViewModelFactory(container))
    val balance by viewModel.balance.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val isProcessing by viewModel.isProcessing.collectAsStateWithLifecycle()
    val snackMessage by viewModel.snackMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var customAmount by remember { mutableStateOf("") }
    var showCustom by remember { mutableStateOf(false) }

    LaunchedEffect(snackMessage) {
        snackMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnack()
        }
    }

    Scaffold(
        topBar = { SSTopBar("Wallet", onBack = onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (isProcessing) {
            Dialog(onDismissRequest = {}) {
                SSCard {
                    Column(Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⏳", fontSize = 40.sp)
                        Spacer(Modifier.height(16.dp))
                        Text("Processing payment...", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }

        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                // Balance card
                val lowBalance = balance < 100
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(
                            if (lowBalance) listOf(SnoutScoutColors.Error, SnoutScoutColors.Error.copy(0.7f))
                            else listOf(SnoutScoutColors.Primary, SnoutScoutColors.PrimaryDark)
                        ))
                        .padding(20.dp)
                ) {
                    Column {
                        Text("Wallet Balance", color = Color.White.copy(0.7f), style = MaterialTheme.typography.labelMedium)
                        Text(CurrencyFormatter.format(balance), color = Color.White,
                            fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
                        Text("≈ ${balance / 12} min at avg. rate", color = Color.White.copy(0.7f),
                            style = MaterialTheme.typography.labelSmall)
                        if (lowBalance) {
                            Spacer(Modifier.height(8.dp))
                            Text("⚠️ Low balance", color = Color.White, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }

            item {
                Text("Recharge Packs", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MockData.RECHARGE_PACKS.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { pack ->
                                SSCard(modifier = Modifier.weight(1f), onClick = { viewModel.recharge(pack.price) }) {
                                    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        if (pack.isPopular) SSBadge("POPULAR", type = BadgeType.PRIMARY)
                                        if (pack.isBestValue) SSBadge("BEST VALUE", type = BadgeType.SUCCESS)
                                        Text(pack.label, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
                                        Text(CurrencyFormatter.format(pack.price), style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold)
                                        Text("${pack.minutes} min", style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { showCustom = !showCustom }, modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Custom Amount")
                }
                if (showCustom) {
                    Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = customAmount, onValueChange = { customAmount = it },
                            label = { Text("Amount (min ₹100)") },
                            modifier = Modifier.weight(1f), singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Button(
                            onClick = {
                                val amt = customAmount.toIntOrNull() ?: 0
                                if (amt >= 100) { viewModel.recharge(amt); showCustom = false; customAmount = "" }
                            },
                            enabled = (customAmount.toIntOrNull() ?: 0) >= 100,
                            shape = RoundedCornerShape(12.dp)
                        ) { Text("Pay") }
                    }
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                SSCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Shield, null, tint = SnoutScoutColors.Success)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Secure payments via Razorpay", style = MaterialTheme.typography.labelMedium)
                            Text("UPI · Cards · Net Banking · Wallets", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
                SSSectionHeader("Transaction History", modifier = Modifier.padding(horizontal = 16.dp))
            }

            if (transactions.isEmpty()) {
                item {
                    Text("No transactions yet", modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                items(transactions) { tx ->
                    ListItem(
                        headlineContent = { Text(tx.label) },
                        supportingContent = {
                            Text("${DateFormatter.formatDate(tx.date)}${tx.method?.let { " · $it" } ?: ""}",
                                style = MaterialTheme.typography.bodySmall)
                        },
                        trailingContent = {
                            Text(
                                if (tx.type == TransactionType.RECHARGE) "+${CurrencyFormatter.format(tx.amount)}"
                                else CurrencyFormatter.format(tx.amount),
                                color = if (tx.type == TransactionType.RECHARGE) SnoutScoutColors.Success else SnoutScoutColors.Error,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}
