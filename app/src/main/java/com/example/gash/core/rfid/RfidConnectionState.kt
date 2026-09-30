package com.example.gash.core.rfid

sealed interface RfidConnectionState {
    data object Disconnected : RfidConnectionState
    data object Connecting : RfidConnectionState
    data object Connected : RfidConnectionState
    data class Error(val message: String, val cause: Throwable? = null) : RfidConnectionState
}