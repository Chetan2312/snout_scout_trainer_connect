package com.chetan.snoutscout.data.model

data class TrainerReview(
    val id: String,
    val trainerId: String,
    val clientName: String,
    val rating: Double,
    val reviewText: String
)