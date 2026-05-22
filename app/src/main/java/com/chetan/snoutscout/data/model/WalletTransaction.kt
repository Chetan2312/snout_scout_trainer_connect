package com.chetan.snoutscout.data.model

data class WalletTransaction(
    val id: String,
    val title: String,
    val amountInInr: Int,
    val type: String,
    val createdAt: String
)