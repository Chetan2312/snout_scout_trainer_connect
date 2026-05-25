package com.snoutscout.app.feature.trainer_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.snoutscout.app.data.model.TrainerProfile
import com.snoutscout.app.data.repository.TrainerRepository
import com.snoutscout.app.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrainerProfileViewModel(private val repo: TrainerRepository) : ViewModel() {
    private val _trainer = MutableStateFlow<TrainerProfile?>(null)
    val trainer = _trainer.asStateFlow()

    fun load(id: String) = viewModelScope.launch {
        _trainer.value = repo.getTrainerById(id)
    }
}

class TrainerProfileViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TrainerProfileViewModel(container.trainerRepository) as T
    }
}
