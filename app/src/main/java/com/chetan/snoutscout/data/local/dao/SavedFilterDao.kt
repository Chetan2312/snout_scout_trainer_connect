package com.chetan.snoutscout.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chetan.snoutscout.data.local.entity.SavedFilterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedFilterDao {

    @Query("SELECT * FROM saved_filters WHERE id = 'active_filter' LIMIT 1")
    fun observeFilter(): Flow<SavedFilterEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(filter: SavedFilterEntity)
}