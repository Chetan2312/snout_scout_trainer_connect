package com.snoutscout.app.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.snoutscout.app.data.model.WalletTransaction
import com.snoutscout.app.data.repository.WalletRepository
import com.snoutscout.app.di.AppContainer
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WalletViewModel(private val repo: WalletRepository) : ViewModel() {
    val balance: StateFlow<Int> = repo.getBalance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val transactions: StateFlow<List<WalletTransaction>> = repo.getTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()

    private val _snackMessage = MutableStateFlow<String?>(null)
    val snackMessage = _snackMessage.asStateFlow()

    fun recharge(amount: Int, method: String = "UPI") = viewModelScope.launch {
        _isProcessing.value = true
        val result = repo.recharge(amount, method)
        _isProcessing.value = false
        if (result.isSuccess) _snackMessage.value = "Recharged ₹$amount successfully!"
        else _snackMessage.value = "Payment failed. Please try again."
    }

    fun clearSnack() { _snackMessage.value = null }
}

class WalletViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return WalletViewModel(container.walletRepository) as T
    }
}
