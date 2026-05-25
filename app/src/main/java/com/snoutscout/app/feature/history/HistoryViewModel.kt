package com.snoutscout.app.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.snoutscout.app.data.model.ConsultationSession
import com.snoutscout.app.data.repository.SessionRepository
import com.snoutscout.app.di.AppContainer
import kotlinx.coroutines.flow.*

class HistoryViewModel(private val repo: SessionRepository) : ViewModel() {
    val sessions: StateFlow<List<ConsultationSession>> = repo.getSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class HistoryViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HistoryViewModel(container.sessionRepository) as T
    }
}
