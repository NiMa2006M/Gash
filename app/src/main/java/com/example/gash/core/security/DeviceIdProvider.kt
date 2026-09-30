package com.example.gash.core.security

interface DeviceIdProvider {
    fun getDeviceId(): String
}