package com.chetan.snoutscout.feature.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chetan.snoutscout.SnoutScoutApp
import com.chetan.snoutscout.core.ui.components.PrimaryButton

@Composable
fun WalletScreen() {
    val application = LocalContext.current.applicationContext as SnoutScoutApp
    val viewModel: WalletViewModel = viewModel(
        factory = WalletViewModel.factory(application.appContainer.walletRepository)
    )
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Wallet balance",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "₹${uiState.balance}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Recharge via UPI or Razorpay-ready flow",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        item {
            Text(
                text = "Recharge packs",
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(uiState.packs, key = { it.id }) { pack ->
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = pack.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "₹${pack.amountInInr}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (pack.bonusText != null) {
                        Text(
                            text = pack.bonusText,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    PrimaryButton(
                        text = "Recharge now",
                        onClick = { }
                    )
                }
            }
        }

        item {
            Text(
                text = "Recent transactions",
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(uiState.transactions, key = { it.id }) { transaction ->
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = transaction.title,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "₹${transaction.amountInInr}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = transaction.createdAt,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}