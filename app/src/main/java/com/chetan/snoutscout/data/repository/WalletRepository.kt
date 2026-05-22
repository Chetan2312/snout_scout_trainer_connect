package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.model.WalletTransaction
import com.chetan.snoutscout.domain.model.RechargePack
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun getBalance(): Flow<Int>
    fun getTransactions(): Flow<List<WalletTransaction>>
    fun getRechargePacks(): Flow<List<RechargePack>>
}