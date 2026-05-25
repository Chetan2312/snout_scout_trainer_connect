package com.snoutscout.app.feature.client_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.snoutscout.app.data.model.TrainerProfile
import com.snoutscout.app.data.repository.TrainerRepository
import com.snoutscout.app.di.AppContainer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val trainerRepository: TrainerRepository) : ViewModel() {
    val topTrainers: StateFlow<List<TrainerProfile>> = trainerRepository.getTrainers()
        .map { it.sortedByDescending { t -> t.rating } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class HomeViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(container.trainerRepository) as T
    }
}
