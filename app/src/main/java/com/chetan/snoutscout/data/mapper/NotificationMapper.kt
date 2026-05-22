package com.chetan.snoutscout.data.mapper

import com.chetan.snoutscout.data.local.entity.NotificationEntity
import com.chetan.snoutscout.data.model.NotificationItem

fun NotificationEntity.toDomain(): NotificationItem {
    return NotificationItem(
        id = id,
        title = title,
        body = body,
        createdAt = createdAt,
        read = read
    )
}

fun NotificationItem.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        title = title,
        body = body,
        createdAt = createdAt,
        read = read
    )
}