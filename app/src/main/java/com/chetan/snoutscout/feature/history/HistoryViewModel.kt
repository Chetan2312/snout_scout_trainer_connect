package com.chetan.snoutscout.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chetan.snoutscout.data.model.ConsultationSession
import com.chetan.snoutscout.data.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HistoryUiState(
    val sessions: List<ConsultationSession> = emptyList()
)

class HistoryViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            sessionRepository.getSessions().collect { sessions ->
                _uiState.value = HistoryUiState(sessions = sessions)
            }
        }
    }

    companion object {
        fun factory(sessionRepository: SessionRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HistoryViewModel(sessionRepository) as T
                }
            }
    }
}