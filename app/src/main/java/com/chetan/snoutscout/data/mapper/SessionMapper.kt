package com.chetan.snoutscout.data.mapper

import com.chetan.snoutscout.data.local.entity.SessionEntity
import com.chetan.snoutscout.data.model.CallType
import com.chetan.snoutscout.data.model.ConsultationSession

fun SessionEntity.toDomain(): ConsultationSession {
    return ConsultationSession(
        id = id,
        trainerName = trainerName,
        dogName = dogName,
        callType = enumValueOrDefault(callType),
        scheduledAt = scheduledAt,
        durationMinutes = durationMinutes,
        totalAmountInInr = totalAmountInInr,
        notesReady = notesReady
    )
}

fun ConsultationSession.toEntity(): SessionEntity {
    return SessionEntity(
        id = id,
        trainerName = trainerName,
        dogName = dogName,
        callType = callType.name,
        scheduledAt = scheduledAt,
        durationMinutes = durationMinutes,
        totalAmountInInr = totalAmountInInr,
        notesReady = notesReady
    )
}

private fun enumValueOrDefault(value: String): CallType {
    return try {
        CallType.valueOf(value)
    } catch (_: IllegalArgumentException) {
        CallType.VOICE
    }
}