package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.model.SessionReport
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun getReports(): Flow<List<SessionReport>>
    suspend fun approveReport(reportId: String)
}