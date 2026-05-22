package com.chetan.snoutscout.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chetan.snoutscout.data.local.dao.DogDao
import com.chetan.snoutscout.data.local.dao.NotificationDao
import com.chetan.snoutscout.data.local.dao.ReportDao
import com.chetan.snoutscout.data.local.dao.SavedFilterDao
import com.chetan.snoutscout.data.local.dao.SessionDao
import com.chetan.snoutscout.data.local.dao.TrainerDao
import com.chetan.snoutscout.data.local.dao.UserSessionDao
import com.chetan.snoutscout.data.local.dao.WalletDao
import com.chetan.snoutscout.data.local.entity.DogEntity
import com.chetan.snoutscout.data.local.entity.NotificationEntity
import com.chetan.snoutscout.data.local.entity.ReportEntity
import com.chetan.snoutscout.data.local.entity.SavedFilterEntity
import com.chetan.snoutscout.data.local.entity.SessionEntity
import com.chetan.snoutscout.data.local.entity.TrainerEntity
import com.chetan.snoutscout.data.local.entity.UserSessionEntity
import com.chetan.snoutscout.data.local.entity.WalletTransactionEntity

@Database(
    entities = [
        UserSessionEntity::class,
        DogEntity::class,
        TrainerEntity::class,
        WalletTransactionEntity::class,
        SessionEntity::class,
        ReportEntity::class,
        NotificationEntity::class,
        SavedFilterEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userSessionDao(): UserSessionDao
    abstract fun dogDao(): DogDao
    abstract fun trainerDao(): TrainerDao
    abstract fun walletDao(): WalletDao
    abstract fun sessionDao(): SessionDao
    abstract fun reportDao(): ReportDao
    abstract fun notificationDao(): NotificationDao
    abstract fun savedFilterDao(): SavedFilterDao
}