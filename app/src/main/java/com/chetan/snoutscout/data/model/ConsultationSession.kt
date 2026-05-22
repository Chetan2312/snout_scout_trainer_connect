package com.chetan.snoutscout.data.model

data class ConsultationSession(
    val id: String,
    val trainerName: String,
    val dogName: String,
    val callType: CallType,
    val scheduledAt: String,
    val durationMinutes: Int,
    val totalAmountInInr: Int,
    val notesReady: Boolean
)