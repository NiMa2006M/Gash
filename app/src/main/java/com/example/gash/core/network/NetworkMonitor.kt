package com.example.gash.core.network

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val isOnline: Flow<Boolean>
    fun isOnlineNow(): Boolean
}