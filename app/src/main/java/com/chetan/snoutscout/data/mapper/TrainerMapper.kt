package com.chetan.snoutscout.data.mapper

import com.chetan.snoutscout.data.local.entity.TrainerEntity
import com.chetan.snoutscout.data.model.Language
import com.chetan.snoutscout.data.model.TrainerProfile
import com.chetan.snoutscout.data.model.TrainerSpecialization
import com.chetan.snoutscout.data.model.VerificationStatus

fun TrainerEntity.toDomain(): TrainerProfile {
    return TrainerProfile(
        id = id,
        fullName = fullName,
        city = city,
        bio = bio,
        yearsOfExperience = yearsOfExperience,
        languages = languages.mapNotNull { value -> enumValueOrNull<Language>(value) },
        specializations = specializations.mapNotNull { value -> enumValueOrNull<TrainerSpecialization>(value) },
        pricePerMinuteInInr = pricePerMinuteInInr,
        rating = rating,
        totalReviews = totalReviews,
        onlineAvailable = onlineAvailable,
        verificationStatus = enumValueOrNull<VerificationStatus>(verificationStatus)
            ?: VerificationStatus.PENDING,
        featured = featured
    )
}

fun TrainerProfile.toEntity(): TrainerEntity {
    return TrainerEntity(
        id = id,
        fullName = fullName,
        city = city,
        bio = bio,
        yearsOfExperience = yearsOfExperience,
        languages = languages.map { it.name },
        specializations = specializations.map { it.name },
        pricePerMinuteInInr = pricePerMinuteInInr,
        rating = rating,
        totalReviews = totalReviews,
        onlineAvailable = onlineAvailable,
        verificationStatus = verificationStatus.name,
        featured = featured
    )
}

private inline fun <reified T : Enum<T>> enumValueOrNull(value: String): T? {
    return enumValues<T>().firstOrNull { it.name == value }
}