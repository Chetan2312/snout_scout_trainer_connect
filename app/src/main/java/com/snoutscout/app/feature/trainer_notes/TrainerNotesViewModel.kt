package com.snoutscout.app.feature.trainer_notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.snoutscout.app.data.model.SessionReport
import com.snoutscout.app.data.repository.ReportRepository
import com.snoutscout.app.di.AppContainer
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TrainerNotesViewModel(private val repo: ReportRepository) : ViewModel() {
    val reports: StateFlow<List<SessionReport>> = repo.getReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun approveReport(report: SessionReport) = viewModelScope.launch { repo.saveReport(report) }
    fun saveReport(report: SessionReport) = viewModelScope.launch { repo.saveReport(report) }
}

class TrainerNotesViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TrainerNotesViewModel(container.reportRepository) as T
    }
}
