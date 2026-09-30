package com.example.gash.domain.usecase.activation

import com.example.gash.core.security.ActivationCodeValidator
import com.example.gash.core.security.DeviceIdProvider
import com.example.gash.domain.error.DomainError
import com.example.gash.domain.repository.ActivationRepository
import javax.inject.Inject

class ActivateDeviceUseCase @Inject constructor(
    private val deviceIdProvider: DeviceIdProvider,
    private val validator: ActivationCodeValidator,
    private val activationRepository: ActivationRepository
) {
    suspend operator fun invoke(
        farmName: String,
        farmId: String,
        phoneNumber: String,
        enteredCode: String
    ): Result<Unit> {
        val deviceId = deviceIdProvider.getDeviceId()
        return if (validator.validate(deviceId, enteredCode)) {
            activationRepository.saveActivation(farmName, farmId, phoneNumber)
            Result.success(Unit)
        } else {
            Result.failure(DomainError.InvalidActivationCode)
        }
    }
}