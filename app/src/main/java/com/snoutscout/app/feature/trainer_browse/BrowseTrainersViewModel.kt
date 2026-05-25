package com.snoutscout.app.feature.trainer_browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.snoutscout.app.data.model.TrainerProfile
import com.snoutscout.app.data.repository.TrainerRepository
import com.snoutscout.app.di.AppContainer
import kotlinx.coroutines.flow.*

data class TrainerFilters(
    val search: String = "",
    val city: String = "All Cities",
    val specialization: String = "Any",
    val onlineOnly: Boolean = false,
    val maxPrice: Int = 25,
    val sortBy: SortBy = SortBy.RATING
)

enum class SortBy { RATING, PRICE, EXPERIENCE }

class BrowseTrainersViewModel(private val repo: TrainerRepository) : ViewModel() {
    private val _filters = MutableStateFlow(TrainerFilters())
    val filters = _filters.asStateFlow()

    val trainers: StateFlow<List<TrainerProfile>> = combine(repo.getTrainers(), _filters) { list, f ->
        list.filter { t ->
            (f.search.isEmpty() || t.name.contains(f.search, true) || t.specializations.any { it.contains(f.search, true) }) &&
            (f.city == "All Cities" || t.city == f.city) &&
            (f.specialization == "Any" || t.specializations.contains(f.specialization)) &&
            (!f.onlineOnly || t.isOnline) &&
            (t.ratePerMin <= f.maxPrice)
        }.sortedWith(when (f.sortBy) {
            SortBy.RATING -> compareByDescending { it.rating }
            SortBy.PRICE -> compareBy { it.ratePerMin }
            SortBy.EXPERIENCE -> compareByDescending { it.experience }
        })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearch(q: String) = _filters.update { it.copy(search = q) }
    fun updateCity(c: String) = _filters.update { it.copy(city = c) }
    fun updateSpec(s: String) = _filters.update { it.copy(specialization = s) }
    fun updateOnlineOnly(v: Boolean) = _filters.update { it.copy(onlineOnly = v) }
    fun updateMaxPrice(p: Int) = _filters.update { it.copy(maxPrice = p) }
    fun updateSort(s: SortBy) = _filters.update { it.copy(sortBy = s) }
    fun resetFilters() = _filters.update { TrainerFilters() }
}

class BrowseTrainersViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BrowseTrainersViewModel(container.trainerRepository) as T
    }
}
