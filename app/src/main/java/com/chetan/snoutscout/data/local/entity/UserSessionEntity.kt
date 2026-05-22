package com.chetan.snoutscout.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_session")
data class UserSessionEntity(
    @PrimaryKey
    val id: String,
    val fullName: String,
    val phoneNumber: String,
    val city: String,
    val role: String
)