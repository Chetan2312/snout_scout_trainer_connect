package com.chetan.snoutscout.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_filters")
data class SavedFilterEntity(
    @PrimaryKey
    val id: String = "active_filter",
    val city: String?,
    val language: String?,
    val breedSpecialization: String?,
    val aggressionSpecialist: Boolean,
    val puppyTrainer: Boolean,
    val obedienceTrainer: Boolean,
    val protectionSports: Boolean,
    val therapyDogs: Boolean,
    val onlineAvailability: Boolean,
    val maxPricePerMinute: Int?,
    val minRating: Double?
)