package com.chetan.snoutscout.data.model

data class NotificationItem(
    val id: String,
    val title: String,
    val body: String,
    val createdAt: String,
    val read: Boolean
)