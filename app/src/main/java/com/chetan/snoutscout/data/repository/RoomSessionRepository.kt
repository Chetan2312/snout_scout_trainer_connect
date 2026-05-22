package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.local.dao.SessionDao
import com.chetan.snoutscout.data.mapper.toDomain
import com.chetan.snoutscout.data.model.ConsultationSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomSessionRepository(
    private val sessionDao: SessionDao
) : SessionRepository {

    override fun getSessions(): Flow<List<ConsultationSession>> {
        return sessionDao.observeSessions().map { items ->
            items.map { it.toDomain() }
        }
    }
}