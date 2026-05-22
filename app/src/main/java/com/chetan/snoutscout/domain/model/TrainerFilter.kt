package com.chetan.snoutscout.domain.model

data class TrainerFilter(
    val city: String? = null,
    val language: String? = null,
    val breedSpecialization: String? = null,
    val aggressionSpecialist: Boolean = false,
    val puppyTrainer: Boolean = false,
    val obedienceTrainer: Boolean = false,
    val protectionSports: Boolean = false,
    val therapyDogs: Boolean = false,
    val onlineAvailability: Boolean = false,
    val maxPricePerMinute: Int? = null,
    val minRating: Double? = null
)