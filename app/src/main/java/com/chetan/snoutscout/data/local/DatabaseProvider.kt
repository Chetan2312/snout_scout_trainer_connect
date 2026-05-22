package com.chetan.snoutscout.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "snout_scout_db"
            ).fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
        }
    }
}