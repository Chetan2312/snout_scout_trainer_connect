package com.chetan.snoutscout.data.mapper

import com.chetan.snoutscout.data.local.entity.WalletTransactionEntity
import com.chetan.snoutscout.data.model.WalletTransaction

fun WalletTransactionEntity.toDomain(): WalletTransaction {
    return WalletTransaction(
        id = id,
        title = title,
        amountInInr = amountInInr,
        type = type,
        createdAt = createdAt
    )
}

fun WalletTransaction.toEntity(): WalletTransactionEntity {
    return WalletTransactionEntity(
        id = id,
        title = title,
        amountInInr = amountInInr,
        type = type,
        createdAt = createdAt
    )
}