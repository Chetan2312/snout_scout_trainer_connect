package com.chetan.snoutscout.feature.trainer_dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chetan.snoutscout.data.model.TrainerProfile
import com.chetan.snoutscout.data.repository.TrainerDashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TrainerDashboardUiState(
    val trainer: TrainerProfile? = null
)

class TrainerDashboardViewModel(
    private val trainerDashboardRepository: TrainerDashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrainerDashboardUiState())
    val uiState: StateFlow<TrainerDashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            trainerDashboardRepository.getPrimaryTrainerProfile().collect { trainer ->
                _uiState.value = TrainerDashboardUiState(trainer = trainer)
            }
        }
    }

    companion object {
        fun factory(trainerDashboardRepository: TrainerDashboardRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TrainerDashboardViewModel(trainerDashboardRepository) as T
                }
            }
    }
}