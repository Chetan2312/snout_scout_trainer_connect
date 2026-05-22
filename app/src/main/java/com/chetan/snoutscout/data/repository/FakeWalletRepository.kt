package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.model.WalletTransaction
import com.chetan.snoutscout.data.service.fake.FakeSeedData
import com.chetan.snoutscout.domain.model.RechargePack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWalletRepository : WalletRepository {
    override fun getBalance(): Flow<Int> = flowOf(FakeSeedData.clientProfile.walletBalanceInInr)

    override fun getTransactions(): Flow<List<WalletTransaction>> = flowOf(FakeSeedData.walletTransactions)

    override fun getRechargePacks(): Flow<List<RechargePack>> = flowOf(FakeSeedData.rechargePacks)
}