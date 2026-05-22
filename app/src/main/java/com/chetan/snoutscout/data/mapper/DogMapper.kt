package com.chetan.snoutscout.data.mapper

import com.chetan.snoutscout.data.local.entity.DogEntity
import com.chetan.snoutscout.data.model.DogProfile

fun DogEntity.toDomain(): DogProfile {
    return DogProfile(
        id = id,
        name = name,
        breed = breed,
        ageInMonths = ageInMonths,
        vaccinationStatus = vaccinationStatus,
        behavioralIssues = behavioralIssues,
        medicalHistory = medicalHistory,
        previousSessionsSummary = previousSessionsSummary
    )
}

fun DogProfile.toEntity(): DogEntity {
    return DogEntity(
        id = id,
        name = name,
        breed = breed,
        ageInMonths = ageInMonths,
        vaccinationStatus = vaccinationStatus,
        behavioralIssues = behavioralIssues,
        medicalHistory = medicalHistory,
        previousSessionsSummary = previousSessionsSummary
    )
}