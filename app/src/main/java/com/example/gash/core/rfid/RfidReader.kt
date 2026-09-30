package com.example.gash.core.rfid

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RfidReader {
    val connectionState: StateFlow<RfidConnectionState>
    val tags: Flow<RfidTag>
    suspend fun connect()
    suspend fun disconnect()
}