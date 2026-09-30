package com.example.gash.core.security

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidIdProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : DeviceIdProvider {

    @SuppressLint("HardwareIds")
    override fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "UNKNOWN_DEVICE"
    }
}