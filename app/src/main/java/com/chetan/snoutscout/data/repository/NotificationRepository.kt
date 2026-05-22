package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.model.NotificationItem
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<List<NotificationItem>>
}