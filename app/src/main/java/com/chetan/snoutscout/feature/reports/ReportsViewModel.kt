package com.chetan.snoutscout.feature.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chetan.snoutscout.data.model.SessionReport
import com.chetan.snoutscout.data.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReportsUiState(
    val reports: List<SessionReport> = emptyList()
)

class ReportsViewModel(
    private val reportRepository: ReportRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            reportRepository.getReports().collect { reports ->
                _uiState.value = ReportsUiState(reports = reports)
            }
        }
    }

    fun approveReport(reportId: String) {
        viewModelScope.launch {
            reportRepository.approveReport(reportId)
        }
    }

    companion object {
        fun factory(reportRepository: ReportRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ReportsViewModel(reportRepository) as T
                }
            }
    }
}