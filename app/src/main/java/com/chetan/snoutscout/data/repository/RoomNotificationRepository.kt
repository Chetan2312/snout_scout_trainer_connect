package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.local.dao.NotificationDao
import com.chetan.snoutscout.data.mapper.toDomain
import com.chetan.snoutscout.data.model.NotificationItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomNotificationRepository(
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override fun getNotifications(): Flow<List<NotificationItem>> {
        return notificationDao.observeNotifications().map { items ->
            items.map { it.toDomain() }
        }
    }
}