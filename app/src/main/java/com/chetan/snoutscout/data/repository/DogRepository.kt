package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.model.DogProfile
import kotlinx.coroutines.flow.Flow

interface DogRepository {
    fun getDogs(): Flow<List<DogProfile>>
}