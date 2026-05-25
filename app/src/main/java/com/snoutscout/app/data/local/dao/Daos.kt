package com.snoutscout.app.data.local.dao

import androidx.room.*
import com.snoutscout.app.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DogProfileDao {
    @Query("SELECT * FROM dog_profiles ORDER BY name ASC")
    fun getAllDogs(): Flow<List<DogProfileEntity>>

    @Query("SELECT * FROM dog_profiles WHERE id = :id")
    suspend fun getDogById(id: String): DogProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDog(dog: DogProfileEntity)

    @Delete
    suspend fun deleteDog(dog: DogProfileEntity)
}

@Dao
interface WalletTransactionDao {
    @Query("SELECT * FROM wallet_transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<WalletTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(tx: WalletTransactionEntity)
}

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions ORDER BY date DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getSessionById(id: String): SessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(session: SessionEntity)
}

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY date DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Query("SELECT * FROM reports WHERE id = :id")
    suspend fun getReportById(id: String): ReportEntity?

    @Query("SELECT * FROM reports WHERE sessionId = :sessionId")
    suspend fun getReportBySessionId(sessionId: String): ReportEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReport(report: ReportEntity)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY date DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)
}

@Dao
interface TrainerDao {
    @Query("SELECT * FROM trainers")
    fun getAllTrainers(): Flow<List<TrainerEntity>>

    @Query("SELECT * FROM trainers WHERE id = :id")
    suspend fun getTrainerById(id: String): TrainerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTrainer(trainer: TrainerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(trainers: List<TrainerEntity>)
}
