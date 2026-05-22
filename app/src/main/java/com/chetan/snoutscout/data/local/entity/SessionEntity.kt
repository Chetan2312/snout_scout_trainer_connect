package com.chetan.snoutscout.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey
    val id: String,
    val trainerName: String,
    val dogName: String,
    val callType: String,
    val scheduledAt: String,
    val durationMinutes: Int,
    val totalAmountInInr: Int,
    val notesReady: Boolean
)