package com.chetan.snoutscout.app

import com.chetan.snoutscout.data.repository.DogRepository
import com.chetan.snoutscout.data.repository.FakeDogRepository
import com.chetan.snoutscout.data.repository.FakeNotificationRepository
import com.chetan.snoutscout.data.repository.FakeTrainerRepository
import com.chetan.snoutscout.data.repository.FakeWalletRepository
import com.chetan.snoutscout.data.repository.NotificationRepository
import com.chetan.snoutscout.data.repository.TrainerRepository
import com.chetan.snoutscout.data.repository.WalletRepository

class AppContainer {
    val trainerRepository: TrainerRepository = FakeTrainerRepository()
    val dogRepository: DogRepository = FakeDogRepository()
    val walletRepository: WalletRepository = FakeWalletRepository()
    val notificationRepository: NotificationRepository = FakeNotificationRepository()
}