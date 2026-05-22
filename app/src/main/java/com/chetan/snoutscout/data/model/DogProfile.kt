package com.chetan.snoutscout.data.model

data class DogProfile(
    val id: String,
    val name: String,
    val breed: String,
    val ageInMonths: Int,
    val vaccinationStatus: String,
    val behavioralIssues: String,
    val medicalHistory: String,
    val previousSessionsSummary: String
)