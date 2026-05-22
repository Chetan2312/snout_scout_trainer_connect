package com.chetan.snoutscout.data.mapper

import com.chetan.snoutscout.data.local.entity.ReportEntity
import com.chetan.snoutscout.data.model.SessionReport

fun ReportEntity.toDomain(): SessionReport {
    return SessionReport(
        id = id,
        sessionId = sessionId,
        dogIssueDiscussed = dogIssueDiscussed,
        trainerObservations = trainerObservations,
        suggestedSolutions = suggestedSolutions,
        dailyRoutine = dailyRoutine,
        trainingInstructions = trainingInstructions,
        followUpRecommendations = followUpRecommendations,
        approved = approved
    )
}

fun SessionReport.toEntity(): ReportEntity {
    return ReportEntity(
        id = id,
        sessionId = sessionId,
        dogIssueDiscussed = dogIssueDiscussed,
        trainerObservations = trainerObservations,
        suggestedSolutions = suggestedSolutions,
        dailyRoutine = dailyRoutine,
        trainingInstructions = trainingInstructions,
        followUpRecommendations = followUpRecommendations,
        approved = approved
    )
}