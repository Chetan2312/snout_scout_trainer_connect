package com.snoutscout.app.feature.dog_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.snoutscout.app.data.model.DogProfile
import com.snoutscout.app.data.repository.DogRepository
import com.snoutscout.app.di.AppContainer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DogViewModel(private val repo: DogRepository) : ViewModel() {
    val dogs: StateFlow<List<DogProfile>> = repo.getDogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveDog(dog: DogProfile) = viewModelScope.launch { repo.saveDog(dog) }
    fun deleteDog(dog: DogProfile) = viewModelScope.launch { repo.deleteDog(dog) }
    suspend fun getDogById(id: String) = repo.getDogById(id)
}

class DogViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DogViewModel(container.dogRepository) as T
    }
}
