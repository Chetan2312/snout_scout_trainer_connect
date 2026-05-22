package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.local.dao.DogDao
import com.chetan.snoutscout.data.mapper.toDomain
import com.chetan.snoutscout.data.model.DogProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomDogRepository(
    private val dogDao: DogDao
) : DogRepository {

    override fun getDogs(): Flow<List<DogProfile>> {
        return dogDao.observeDogs().map { items ->
            items.map { it.toDomain() }
        }
    }
}