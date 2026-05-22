package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.model.ConsultationSession
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getSessions(): Flow<List<ConsultationSession>>
}