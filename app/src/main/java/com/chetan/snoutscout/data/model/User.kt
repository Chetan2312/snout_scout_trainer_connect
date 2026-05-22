package com.chetan.snoutscout.data.model

import com.chetan.snoutscout.domain.model.UserRole

data class User(
    val id: String,
    val fullName: String,
    val phoneNumber: String,
    val city: String,
    val role: UserRole
)