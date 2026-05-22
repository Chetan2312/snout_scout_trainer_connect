package com.chetan.snoutscout.app

import android.content.Context
import com.chetan.snoutscout.data.local.DatabaseProvider
import com.chetan.snoutscout.data.repository.DogRepository
import com.chetan.snoutscout.data.repository.NotificationRepository
import com.chetan.snoutscout.data.repository.ReportRepository
import com.chetan.snoutscout.data.repository.RoomDogRepository
import com.chetan.snoutscout.data.repository.RoomNotificationRepository
import com.chetan.snoutscout.data.repository.RoomReportRepository
import com.chetan.snoutscout.data.repository.RoomSessionRepository
import com.chetan.snoutscout.data.repository.RoomTrainerDashboardRepository
import com.chetan.snoutscout.data.repository.RoomTrainerRepository
import com.chetan.snoutscout.data.repository.RoomWalletRepository
import com.chetan.snoutscout.data.repository.SessionRepository
import com.chetan.snoutscout.data.repository.TrainerDashboardRepository
import com.chetan.snoutscout.data.repository.TrainerRepository
import com.chetan.snoutscout.data.repository.WalletRepository
import com.chetan.snoutscout.mock.SeedLoader

class AppContainer(
    context: Context
) {
    private val database = DatabaseProvider.getDatabase(context)

    val trainerRepository: TrainerRepository = RoomTrainerRepository(database.trainerDao())
    val dogRepository: DogRepository = RoomDogRepository(database.dogDao())
    val walletRepository: WalletRepository = RoomWalletRepository(database.walletDao())
    val notificationRepository: NotificationRepository = RoomNotificationRepository(database.notificationDao())
    val sessionRepository: SessionRepository = RoomSessionRepository(database.sessionDao())
    val reportRepository: ReportRepository = RoomReportRepository(database.reportDao())
    val trainerDashboardRepository: TrainerDashboardRepository =
        RoomTrainerDashboardRepository(database.trainerDao())

    val seedLoader = SeedLoader(database)
}