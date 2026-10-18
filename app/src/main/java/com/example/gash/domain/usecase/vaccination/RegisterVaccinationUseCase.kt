package com.example.gash.domain.usecase.vaccination

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.model.VaccinationRegistration
import com.example.gash.domain.model.VaccinationSettings
import com.example.gash.domain.repository.AnimalRepository
import com.example.gash.domain.repository.RfidTagRepository
import com.example.gash.domain.repository.VaccinationRepository
import com.example.gash.domain.transaction.TransactionRunner
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class RegisterVaccinationUseCase @Inject constructor(
    private val vaccinationRepository: VaccinationRepository,
    private val rfidTagRepository: RfidTagRepository,
    private val animalRepository: AnimalRepository,
    private val transactionRunner: TransactionRunner
) {
    suspend operator fun invoke(
        operationId: Long?,
        settings: VaccinationSettings,
        rfidCode: String,
        allowHerdMismatch: Boolean = false
    ): Result<VaccinationRegistration> {
        val code = rfidCode.trim()
        if (code.isEmpty()) return Result.failure(DomainError.EmptyRfidCode)

        val cleanSettings = settings.normalized()
        if (cleanSettings.doseMl != null && cleanSettings.doseMl <= 0.0) {
            return Result.failure(DomainError.InvalidDose)
        }

        return try {
            val registration = transactionRunner.run {
                val existing = operationId?.let { vaccinationRepository.getOperation(it) }
                if (operationId != null && (existing == null || !existing.isOpen)) {
                    throw DomainError.VaccinationOperationClosed
                }

                val tag = rfidTagRepository.findActiveByCode(code) ?: throw DomainError.RfidTagNotFound
                val animalId = tag.animalId ?: throw DomainError.RfidTagNotFound
                val animal = animalRepository.getAnimal(animalId) ?: throw DomainError.AnimalNotFound
                if (animal.expiredAt != null) throw DomainError.AnimalExpired

                val expectedHerdId = existing?.herdId ?: cleanSettings.herdId
                if (expectedHerdId != null && animal.herdId != expectedHerdId && !allowHerdMismatch) {
                    throw DomainError.AnimalNotInSelectedHerd(animalId)
                }

                val resolvedOperationId =
                    existing?.id ?: vaccinationRepository.createOperation(cleanSettings)

                if (vaccinationRepository.hasRecord(resolvedOperationId, animalId)) {
                    throw DomainError.AnimalAlreadyVaccinatedInOperation
                }
                vaccinationRepository.addRecord(resolvedOperationId, animalId, code)

                val operation = vaccinationRepository.getOperation(resolvedOperationId)
                    ?: throw DomainError.VaccinationOperationClosed
                VaccinationRegistration(operation, animalId)
            }
            Result.success(registration)
        } catch (e: CancellationException) {
            throw e
        } catch (e: DomainError) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(DomainError.Unknown(e))
        }
    }
}