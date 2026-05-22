package com.chetan.snoutscout.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chetan.snoutscout.data.local.entity.WalletTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {

    @Query("SELECT * FROM wallet_transactions ORDER BY createdAt DESC")
    fun observeTransactions(): Flow<List<WalletTransactionEntity>>

    @Query("SELECT COALESCE(SUM(amountInInr), 0) FROM wallet_transactions")
    fun observeBalance(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<WalletTransactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WalletTransactionEntity)
}