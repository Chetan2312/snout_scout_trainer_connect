package com.chetan.snoutscout.data.model

data class SessionReport(
    val id: String,
    val sessionId: String,
    val dogIssueDiscussed: String,
    val trainerObservations: String,
    val suggestedSolutions: String,
    val dailyRoutine: String,
    val trainingInstructions: String,
    val followUpRecommendations: String,
    val approved: Boolean
)