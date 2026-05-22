package com.chetan.snoutscout.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chetan.snoutscout.data.model.WalletTransaction
import com.chetan.snoutscout.data.repository.WalletRepository
import com.chetan.snoutscout.domain.model.RechargePack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WalletUiState(
    val balance: Int = 0,
    val transactions: List<WalletTransaction> = emptyList(),
    val packs: List<RechargePack> = emptyList()
)

class WalletViewModel(
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            walletRepository.getBalance().collect { balance ->
                _uiState.value = _uiState.value.copy(balance = balance)
            }
        }
        viewModelScope.launch {
            walletRepository.getTransactions().collect { transactions ->
                _uiState.value = _uiState.value.copy(transactions = transactions)
            }
        }
        viewModelScope.launch {
            walletRepository.getRechargePacks().collect { packs ->
                _uiState.value = _uiState.value.copy(packs = packs)
            }
        }
    }

    companion object {
        fun factory(walletRepository: WalletRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return WalletViewModel(walletRepository) as T
                }
            }
    }
}