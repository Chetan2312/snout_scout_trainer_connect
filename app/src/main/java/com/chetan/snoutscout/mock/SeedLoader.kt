package com.chetan.snoutscout.mock

import com.chetan.snoutscout.data.local.AppDatabase
import com.chetan.snoutscout.data.local.entity.NotificationEntity
import com.chetan.snoutscout.data.local.entity.UserSessionEntity
import com.chetan.snoutscout.data.mapper.toEntity
import com.chetan.snoutscout.data.service.fake.FakeSeedData

class SeedLoader(
    private val database: AppDatabase
) {

    suspend fun seedIfEmpty() {

        database.userSessionDao().upsertSession(
            UserSessionEntity(
                id = FakeSeedData.currentUser.id,
                fullName = FakeSeedData.currentUser.fullName,
                phoneNumber = FakeSeedData.currentUser.phoneNumber,
                city = FakeSeedData.currentUser.city,
                role = FakeSeedData.currentUser.role.name
            )
        )

        database.trainerDao().upsertAll(
            FakeSeedData.trainers.map { it.toEntity() }
        )

        database.dogDao().upsertAll(
            FakeSeedData.dogProfiles.map { it.toEntity() }
        )

        database.walletDao().upsertAll(
            FakeSeedData.walletTransactions.map { it.toEntity() }
        )

        database.sessionDao().upsertAll(
            FakeSeedData.sessions.map {
                com.chetan.snoutscout.data.local.entity.SessionEntity(
                    id = it.id,
                    trainerName = it.trainerName,
                    dogName = it.dogName,
                    callType = it.callType.name,
                    scheduledAt = it.scheduledAt,
                    durationMinutes = it.durationMinutes,
                    totalAmountInInr = it.totalAmountInInr,
                    notesReady = it.notesReady
                )
            }
        )

        database.reportDao().upsertAll(
            FakeSeedData.reports.map {
                com.chetan.snoutscout.data.local.entity.ReportEntity(
                    id = it.id,
                    sessionId = it.sessionId,
                    dogIssueDiscussed = it.dogIssueDiscussed,
                    trainerObservations = it.trainerObservations,
                    suggestedSolutions = it.suggestedSolutions,
                    dailyRoutine = it.dailyRoutine,
                    trainingInstructions = it.trainingInstructions,
                    followUpRecommendations = it.followUpRecommendations,
                    approved = it.approved
                )
            }
        )

        database.notificationDao().upsertAll(
            FakeSeedData.notifications.map { it.toEntity() }
        )
    }
}