package com.chetan.snoutscout.feature.dog_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chetan.snoutscout.data.model.DogProfile
import com.chetan.snoutscout.data.repository.DogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DogProfileUiState(
    val dogs: List<DogProfile> = emptyList()
)

class DogProfileViewModel(
    private val dogRepository: DogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DogProfileUiState())
    val uiState: StateFlow<DogProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dogRepository.getDogs().collect { dogs ->
                _uiState.value = DogProfileUiState(dogs = dogs)
            }
        }
    }

    companion object {
        fun factory(dogRepository: DogRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DogProfileViewModel(dogRepository) as T
                }
            }
    }
}