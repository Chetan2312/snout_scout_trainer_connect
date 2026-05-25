package com.snoutscout.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dog_profiles")
data class DogProfileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val breed: String,
    val age: String,
    val gender: String,
    val weight: String,
    val vaccination: String,
    val lastVaccination: String,
    val issues: List<String>,
    val medicalHistory: String,
    val previousSessions: Int,
    val imageUri: String? = null
)

@Entity(tableName = "wallet_transactions")
data class WalletTransactionEntity(
    @PrimaryKey val id: String,
    val type: String,
    val amount: Int,
    val method: String? = null,
    val date: Long,
    val label: String,
    val sessionId: String? = null
)

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val id: String,
    val trainerId: String,
    val trainerName: String,
    val dogId: String,
    val dogName: String,
    val type: String,
    val durationMinutes: Int,
    val cost: Int,
    val date: Long,
    val status: String,
    val rating: Int? = null,
    val hasReport: Boolean,
    val hasChat: Boolean,
    val summary: String
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val trainerName: String,
    val dogName: String,
    val date: String,
    val status: String,
    val issueDiscussed: String,
    val observations: String,
    val solutions: String,
    val routine: String,
    val instructions: String,
    val followUp: String
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val type: String,
    val title: String,
    val body: String,
    val date: Long,
    val isRead: Boolean
)

@Entity(tableName = "trainers")
data class TrainerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val city: String,
    val rating: Float,
    val reviewCount: Int,
    val ratePerMin: Int,
    val experience: Int,
    val specializations: List<String>,
    val languages: List<String>,
    val breeds: List<String>,
    val bio: String,
    val isVerified: Boolean,
    val isOnline: Boolean,
    val isFeatured: Boolean,
    val certifications: List<String>,
    val totalSessions: Int,
    val responseTime: String
)
