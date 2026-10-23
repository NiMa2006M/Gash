package com.example.gash.domain.usecase.network

import com.example.gash.core.network.NetworkMonitor
import javax.inject.Inject

class CheckInternetUseCase @Inject constructor(
    private val networkMonitor: NetworkMonitor
) {
    operator fun invoke(): Boolean = networkMonitor.isOnlineNow()
}