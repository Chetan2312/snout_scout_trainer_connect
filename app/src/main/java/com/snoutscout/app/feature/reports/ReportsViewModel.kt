package com.snoutscout.app.feature.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.snoutscout.app.data.model.SessionReport
import com.snoutscout.app.data.repository.ReportRepository
import com.snoutscout.app.di.AppContainer
import kotlinx.coroutines.flow.*

class ReportsViewModel(private val repo: ReportRepository) : ViewModel() {
    val reports: StateFlow<List<SessionReport>> = repo.getReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class ReportsViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ReportsViewModel(container.reportRepository) as T
    }
}
