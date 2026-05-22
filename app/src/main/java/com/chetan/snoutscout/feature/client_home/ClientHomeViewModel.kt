package com.chetan.snoutscout.feature.client_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chetan.snoutscout.data.model.TrainerProfile
import com.chetan.snoutscout.data.repository.TrainerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ClientHomeUiState(
    val featuredTrainers: List<TrainerProfile> = emptyList()
)

class ClientHomeViewModel(
    private val trainerRepository: TrainerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientHomeUiState())
    val uiState: StateFlow<ClientHomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            trainerRepository.getFeaturedTrainers().collect { trainers ->
                _uiState.value = ClientHomeUiState(featuredTrainers = trainers)
            }
        }
    }

    companion object {
        fun factory(trainerRepository: TrainerRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ClientHomeViewModel(trainerRepository) as T
                }
            }
    }
}