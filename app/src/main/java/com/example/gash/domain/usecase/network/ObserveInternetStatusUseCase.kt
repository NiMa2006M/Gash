package com.example.gash.domain.usecase.network

import com.example.gash.core.network.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveInternetStatusUseCase @Inject constructor(
    private val networkMonitor: NetworkMonitor
) {
    operator fun invoke(): Flow<Boolean> = networkMonitor.isOnline
}