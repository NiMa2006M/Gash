package com.example.gash.domain.usecase.vaccination

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.repository.VaccinationRepository
import com.example.gash.domain.transaction.TransactionRunner
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class RemoveVaccinationRecordUseCase @Inject constructor(
    private val repository: VaccinationRepository,
    private val transactionRunner: TransactionRunner
) {
    suspend operator fun invoke(recordId: Long): Result<Boolean> {
        return try {
            val operationDeleted = transactionRunner.run {
                val record = repository.getRecord(recordId)
                    ?: throw DomainError.VaccinationRecordNotFound
                val operation = repository.getOperation(record.operationId)
                if (operation == null || !operation.isOpen) {
                    throw DomainError.VaccinationOperationClosed
                }

                repository.deleteRecord(recordId)
                if (repository.countRecords(operation.id) == 0) {
                    repository.deleteOperation(operation.id)
                    true
                } else false
            }
            Result.success(operationDeleted)
        } catch (e: CancellationException) {
            throw e
        } catch (e: DomainError) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(DomainError.Unknown(e))
        }
    }
}