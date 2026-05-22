package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.model.TrainerProfile
import kotlinx.coroutines.flow.Flow

interface TrainerDashboardRepository {
    fun getPrimaryTrainerProfile(): Flow<TrainerProfile?>
}