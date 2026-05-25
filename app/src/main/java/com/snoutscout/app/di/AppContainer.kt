package com.snoutscout.app.di

import android.content.Context
import com.snoutscout.app.data.local.AppDatabase
import com.snoutscout.app.data.repository.*
import com.snoutscout.app.service.*

class AppContainer(context: Context) {
    private val db = AppDatabase.getInstance(context)

    val callService: CallService = MockCallService()
    val paymentGateway = MockPaymentGateway()
    val aiSummarizer: AISummarizer = MockAISummarizer()

    val trainerRepository = TrainerRepository(db.trainerDao())
    val dogRepository = DogRepository(db.dogProfileDao())
    val walletRepository = WalletRepository(db.walletTransactionDao(), paymentGateway)
    val sessionRepository = SessionRepository(db.sessionDao())
    val reportRepository = ReportRepository(db.reportDao())
    val notificationRepository = NotificationRepository(db.notificationDao())
}
