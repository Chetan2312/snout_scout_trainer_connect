package com.snoutscout.app.data.local

import android.content.Context
import androidx.room.*
import com.snoutscout.app.data.local.converter.Converters
import com.snoutscout.app.data.local.dao.*
import com.snoutscout.app.data.local.entity.*
import com.snoutscout.app.data.model.MockData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        DogProfileEntity::class,
        WalletTransactionEntity::class,
        SessionEntity::class,
        ReportEntity::class,
        NotificationEntity::class,
        TrainerEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dogProfileDao(): DogProfileDao
    abstract fun walletTransactionDao(): WalletTransactionDao
    abstract fun sessionDao(): SessionDao
    abstract fun reportDao(): ReportDao
    abstract fun notificationDao(): NotificationDao
    abstract fun trainerDao(): TrainerDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                var dbRef: AppDatabase? = null
                val callback = object : RoomDatabase.Callback() {
                    override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            dbRef?.let { prepopulate(it) }
                        }
                    }
                }
                Room.databaseBuilder(context, AppDatabase::class.java, "snoutscout.db")
                    .addCallback(callback)
                    .build()
                    .also { db -> dbRef = db; instance = db }
            }
        }

        private suspend fun prepopulate(db: AppDatabase) {
            MockData.DOGS.forEach { dog ->
                db.dogProfileDao().upsertDog(
                    DogProfileEntity(dog.id, dog.name, dog.breed, dog.age, dog.gender, dog.weight,
                        dog.vaccination, dog.lastVaccination, dog.issues, dog.medicalHistory, dog.previousSessions, dog.imageUri)
                )
            }
            MockData.WALLET_TRANSACTIONS.forEach { tx ->
                db.walletTransactionDao().insertTransaction(
                    WalletTransactionEntity(tx.id, tx.type.name, tx.amount, tx.method, tx.date, tx.label, tx.sessionId)
                )
            }
            MockData.SESSIONS.forEach { s ->
                db.sessionDao().upsertSession(
                    SessionEntity(s.id, s.trainerId, s.trainerName, s.dogId, s.dogName,
                        s.type.name, s.durationMinutes, s.cost, s.date, s.status.name, s.rating, s.hasReport, s.hasChat, s.summary)
                )
            }
            MockData.REPORTS.forEach { r ->
                db.reportDao().upsertReport(
                    ReportEntity(r.id, r.sessionId, r.trainerName, r.dogName, r.date, r.status.name,
                        r.issueDiscussed, r.observations, r.solutions, r.routine, r.instructions, r.followUp)
                )
            }
            MockData.NOTIFICATIONS.forEach { n ->
                db.notificationDao().upsertNotification(
                    NotificationEntity(n.id, n.type.name, n.title, n.body, n.date, n.isRead)
                )
            }
            db.trainerDao().upsertAll(MockData.TRAINERS.map { t ->
                TrainerEntity(t.id, t.name, t.city, t.rating, t.reviewCount, t.ratePerMin, t.experience,
                    t.specializations, t.languages, t.breeds, t.bio, t.isVerified, t.isOnline, t.isFeatured,
                    t.certifications, t.totalSessions, t.responseTime)
            })
        }
    }
}
