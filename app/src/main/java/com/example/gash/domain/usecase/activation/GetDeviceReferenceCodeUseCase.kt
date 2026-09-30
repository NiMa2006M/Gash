package com.example.gash.domain.usecase.activation

import com.example.gash.core.security.DeviceIdProvider
import javax.inject.Inject

class GetDeviceReferenceCodeUseCase @Inject constructor(
    private val deviceIdProvider: DeviceIdProvider
) {
    operator fun invoke(): String =
        deviceIdProvider.getDeviceId().takeLast(5).uppercase()
}