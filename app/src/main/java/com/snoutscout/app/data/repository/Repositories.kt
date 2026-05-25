package com.snoutscout.app.data.repository

import com.snoutscout.app.data.local.dao.*
import com.snoutscout.app.data.local.entity.*
import com.snoutscout.app.data.model.*
import com.snoutscout.app.service.PaymentGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class TrainerRepository(private val trainerDao: TrainerDao) {
    fun getTrainers(): Flow<List<TrainerProfile>> = trainerDao.getAllTrainers().map { list ->
        list.map { it.toModel() }
    }

    suspend fun getTrainerById(id: String): TrainerProfile? = trainerDao.getTrainerById(id)?.toModel()

    private fun TrainerEntity.toModel() = TrainerProfile(
        id, name, city, rating, reviewCount, ratePerMin, experience, specializations, languages,
        breeds, bio, isVerified, isOnline, isFeatured, certifications, totalSessions, responseTime
    )
}

class DogRepository(private val dogDao: DogProfileDao) {
    fun getDogs(): Flow<List<DogProfile>> = dogDao.getAllDogs().map { list -> list.map { it.toModel() } }

    suspend fun getDogById(id: String): DogProfile? = dogDao.getDogById(id)?.toModel()

    suspend fun saveDog(dog: DogProfile) {
        dogDao.upsertDog(
            DogProfileEntity(dog.id, dog.name, dog.breed, dog.age, dog.gender, dog.weight,
                dog.vaccination, dog.lastVaccination, dog.issues, dog.medicalHistory, dog.previousSessions, dog.imageUri)
        )
    }

    suspend fun deleteDog(dog: DogProfile) {
        dogDao.deleteDog(
            DogProfileEntity(dog.id, dog.name, dog.breed, dog.age, dog.gender, dog.weight,
                dog.vaccination, dog.lastVaccination, dog.issues, dog.medicalHistory, dog.previousSessions, dog.imageUri)
        )
    }

    private fun DogProfileEntity.toModel() = DogProfile(id, name, breed, age, gender, weight, vaccination, lastVaccination, issues, medicalHistory, previousSessions, imageUri)
}

class WalletRepository(
    private val walletDao: WalletTransactionDao,
    private val paymentGateway: PaymentGateway
) {
    fun getBalance(): Flow<Int> = walletDao.getAllTransactions().map { list -> list.sumOf { it.amount } }

    fun getTransactions(): Flow<List<WalletTransaction>> = walletDao.getAllTransactions().map { list ->
        list.map { WalletTransaction(it.id, TransactionType.valueOf(it.type), it.amount, it.method, it.date, it.label, it.sessionId) }
    }

    suspend fun recharge(amount: Int, method: String): Result<Unit> {
        val result = paymentGateway.initiatePayment(amount, method)
        return if (result.isSuccess) {
            walletDao.insertTransaction(
                WalletTransactionEntity(UUID.randomUUID().toString(), TransactionType.RECHARGE.name,
                    amount, method, System.currentTimeMillis(), "Wallet Recharge", null)
            )
            Result.success(Unit)
        } else Result.failure(result.exceptionOrNull() ?: Exception("Payment failed"))
    }

    suspend fun deduct(amount: Int, label: String, sessionId: String?): Result<Unit> {
        walletDao.insertTransaction(
            WalletTransactionEntity(UUID.randomUUID().toString(), TransactionType.DEDUCTION.name,
                -amount, null, System.currentTimeMillis(), label, sessionId)
        )
        return Result.success(Unit)
    }
}

class SessionRepository(private val sessionDao: SessionDao) {
    fun getSessions(): Flow<List<ConsultationSession>> = sessionDao.getAllSessions().map { list -> list.map { it.toModel() } }

    suspend fun getSessionById(id: String): ConsultationSession? = sessionDao.getSessionById(id)?.toModel()

    suspend fun saveSession(session: ConsultationSession) {
        sessionDao.upsertSession(
            SessionEntity(session.id, session.trainerId, session.trainerName, session.dogId, session.dogName,
                session.type.name, session.durationMinutes, session.cost, session.date, session.status.name,
                session.rating, session.hasReport, session.hasChat, session.summary)
        )
    }

    private fun SessionEntity.toModel() = ConsultationSession(id, trainerId, trainerName, dogId, dogName,
        CallType.valueOf(type), durationMinutes, cost, date, SessionStatus.valueOf(status), rating, hasReport, hasChat, summary)
}

class ReportRepository(private val reportDao: ReportDao) {
    fun getReports(): Flow<List<SessionReport>> = reportDao.getAllReports().map { list -> list.map { it.toModel() } }

    suspend fun getReportById(id: String): SessionReport? = reportDao.getReportById(id)?.toModel()

    suspend fun saveReport(report: SessionReport) {
        reportDao.upsertReport(
            ReportEntity(report.id, report.sessionId, report.trainerName, report.dogName, report.date,
                report.status.name, report.issueDiscussed, report.observations, report.solutions,
                report.routine, report.instructions, report.followUp)
        )
    }

    private fun ReportEntity.toModel() = SessionReport(id, sessionId, trainerName, dogName, date,
        ReportStatus.valueOf(status), issueDiscussed, observations, solutions, routine, instructions, followUp)
}

class NotificationRepository(private val notificationDao: NotificationDao) {
    fun getNotifications(): Flow<List<NotificationItem>> = notificationDao.getAllNotifications().map { list ->
        list.map { NotificationItem(it.id, NotificationType.valueOf(it.type), it.title, it.body, it.date, it.isRead) }
    }

    fun getUnreadCount(): Flow<Int> = notificationDao.getUnreadCount()

    suspend fun markAsRead(id: String) = notificationDao.markAsRead(id)
}
