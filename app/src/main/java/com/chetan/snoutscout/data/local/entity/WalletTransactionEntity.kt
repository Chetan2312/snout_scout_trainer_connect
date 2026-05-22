package com.chetan.snoutscout.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet_transactions")
data class WalletTransactionEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val amountInInr: Int,
    val type: String,
    val createdAt: String
)