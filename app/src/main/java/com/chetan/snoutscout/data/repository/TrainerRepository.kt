package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.model.TrainerProfile
import com.chetan.snoutscout.domain.model.TrainerFilter
import kotlinx.coroutines.flow.Flow

interface TrainerRepository {
    fun getAllTrainers(): Flow<List<TrainerProfile>>
    fun getFilteredTrainers(filter: TrainerFilter): Flow<List<TrainerProfile>>
    fun getFeaturedTrainers(): Flow<List<TrainerProfile>>
}