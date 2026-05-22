package com.chetan.snoutscout.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dogs")
data class DogEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val breed: String,
    val ageInMonths: Int,
    val vaccinationStatus: String,
    val behavioralIssues: String,
    val medicalHistory: String,
    val previousSessionsSummary: String
)