package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.local.dao.WalletDao
import com.chetan.snoutscout.data.mapper.toDomain
import com.chetan.snoutscout.data.model.WalletTransaction
import com.chetan.snoutscout.data.service.fake.FakeSeedData
import com.chetan.snoutscout.domain.model.RechargePack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomWalletRepository(
    private val walletDao: WalletDao
) : WalletRepository {

    override fun getBalance(): Flow<Int> {
        return walletDao.observeBalance()
    }

    override fun getTransactions(): Flow<List<WalletTransaction>> {
        return walletDao.observeTransactions().map { items ->
            items.map { it.toDomain() }
        }
    }

    override fun getRechargePacks(): Flow<List<RechargePack>> {
        return kotlinx.coroutines.flow.flowOf(FakeSeedData.rechargePacks)
    }
}