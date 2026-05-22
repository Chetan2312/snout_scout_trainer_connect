package com.chetan.snoutscout.data.model

data class ClientProfile(
    val userId: String,
    val preferredLanguage: Language,
    val walletBalanceInInr: Int
)