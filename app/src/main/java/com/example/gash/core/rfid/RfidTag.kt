package com.example.gash.core.rfid

data class RfidTag(
    val code: String,
    val readAt: Long,
    val rssi: Int? = null
)