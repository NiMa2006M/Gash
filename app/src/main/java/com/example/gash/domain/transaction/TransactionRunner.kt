package com.example.gash.domain.transaction

interface TransactionRunner {
    suspend fun <T> run(block: suspend () -> T): T
}