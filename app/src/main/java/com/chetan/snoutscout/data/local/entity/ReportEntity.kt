package com.chetan.snoutscout.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey
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