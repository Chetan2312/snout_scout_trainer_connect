package com.snoutscout.app

import androidx.lifecycle.ViewModel
import com.snoutscout.app.data.model.AppState
import com.snoutscout.app.data.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {
    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    fun login() = _appState.update { it.copy(isLoggedIn = true) }

    fun logout() = _appState.update { AppState() }

    fun switchRole() = _appState.update { state ->
        state.copy(
            currentRole = if (state.currentRole == UserRole.CLIENT) UserRole.TRAINER else UserRole.CLIENT
        )
    }

    fun updateBalance(newBalance: Int) = _appState.update { it.copy(walletBalance = newBalance) }
}
