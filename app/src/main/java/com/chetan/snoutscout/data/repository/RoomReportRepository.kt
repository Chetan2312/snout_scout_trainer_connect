package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.local.dao.ReportDao
import com.chetan.snoutscout.data.local.entity.ReportEntity
import com.chetan.snoutscout.data.mapper.toDomain
import com.chetan.snoutscout.data.model.SessionReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class RoomReportRepository(
    private val reportDao: ReportDao
) : ReportRepository {

    override fun getReports(): Flow<List<SessionReport>> {
        return reportDao.observeReports().map { items ->
            items.map { it.toDomain() }
        }
    }

    override suspend fun approveReport(reportId: String) {
        val current = reportDao.observeReports().first().firstOrNull { it.id == reportId } ?: return
        reportDao.upsert(
            ReportEntity(
                id = current.id,
                sessionId = current.sessionId,
                dogIssueDiscussed = current.dogIssueDiscussed,
                trainerObservations = current.trainerObservations,
                suggestedSolutions = current.suggestedSolutions,
                dailyRoutine = current.dailyRoutine,
                trainingInstructions = current.trainingInstructions,
                followUpRecommendations = current.followUpRecommendations,
                approved = true
            )
        )
    }
}