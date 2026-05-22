package com.chetan.snoutscout.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trainers")
data class TrainerEntity(
    @PrimaryKey
    val id: String,
    val fullName: String,
    val city: String,
    val bio: String,
    val yearsOfExperience: Int,
    val languages: List<String>,
    val specializations: List<String>,
    val pricePerMinuteInInr: Int,
    val rating: Double,
    val totalReviews: Int,
    val onlineAvailable: Boolean,
    val verificationStatus: String,
    val featured: Boolean
)