package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.model.NotificationItem
import com.chetan.snoutscout.data.service.fake.FakeSeedData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeNotificationRepository : NotificationRepository {
    override fun getNotifications(): Flow<List<NotificationItem>> = flowOf(FakeSeedData.notifications)
}