package com.chetan.snoutscout.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chetan.snoutscout.data.local.entity.TrainerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainerDao {

    @Query("SELECT * FROM trainers ORDER BY featured DESC, rating DESC")
    fun observeAll(): Flow<List<TrainerEntity>>

    @Query("SELECT * FROM trainers WHERE featured = 1 ORDER BY rating DESC")
    fun observeFeatured(): Flow<List<TrainerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<TrainerEntity>)
}