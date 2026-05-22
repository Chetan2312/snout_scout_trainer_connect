package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.local.dao.TrainerDao
import com.chetan.snoutscout.data.mapper.toDomain
import com.chetan.snoutscout.data.model.TrainerProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomTrainerDashboardRepository(
    private val trainerDao: TrainerDao
) : TrainerDashboardRepository {

    override fun getPrimaryTrainerProfile(): Flow<TrainerProfile?> {
        return trainerDao.observeFeatured().map { items ->
            items.firstOrNull()?.toDomain()
        }
    }
}