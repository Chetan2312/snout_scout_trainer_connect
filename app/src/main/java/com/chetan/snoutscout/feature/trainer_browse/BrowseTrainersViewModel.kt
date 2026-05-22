package com.chetan.snoutscout.feature.trainer_browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chetan.snoutscout.data.model.TrainerProfile
import com.chetan.snoutscout.data.repository.TrainerRepository
import com.chetan.snoutscout.domain.model.TrainerFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BrowseTrainersUiState(
    val trainers: List<TrainerProfile> = emptyList(),
    val activeFilter: TrainerFilter = TrainerFilter()
)

class BrowseTrainersViewModel(
    private val trainerRepository: TrainerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrowseTrainersUiState())
    val uiState: StateFlow<BrowseTrainersUiState> = _uiState.asStateFlow()

    init {
        loadAll()
    }

    fun loadAll() {
        viewModelScope.launch {
            trainerRepository.getAllTrainers().collect { trainers ->
                _uiState.value = BrowseTrainersUiState(
                    trainers = trainers,
                    activeFilter = TrainerFilter()
                )
            }
        }
    }

    fun applyFilter(filter: TrainerFilter) {
        viewModelScope.launch {
            trainerRepository.getFilteredTrainers(filter).collect { trainers ->
                _uiState.value = BrowseTrainersUiState(
                    trainers = trainers,
                    activeFilter = filter
                )
            }
        }
    }

    companion object {
        fun factory(trainerRepository: TrainerRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return BrowseTrainersViewModel(trainerRepository) as T
                }
            }
    }
}