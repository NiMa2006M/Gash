package com.example.gash.data.transaction

import androidx.room.withTransaction
import com.example.gash.core.database.AppDatabase
import com.example.gash.domain.transaction.TransactionRunner
import javax.inject.Inject

class RoomTransactionRunner @Inject constructor(
    private val database: AppDatabase
) : TransactionRunner {
    override suspend fun <T> run(block: suspend () -> T): T = database.withTransaction { block() }
}