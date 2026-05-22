package com.chetan.snoutscout.data.model

data class TrainerProfile(
    val id: String,
    val fullName: String,
    val city: String,
    val bio: String,
    val yearsOfExperience: Int,
    val languages: List<Language>,
    val specializations: List<TrainerSpecialization>,
    val pricePerMinuteInInr: Int,
    val rating: Double,
    val totalReviews: Int,
    val onlineAvailable: Boolean,
    val verificationStatus: VerificationStatus,
    val featured: Boolean
)