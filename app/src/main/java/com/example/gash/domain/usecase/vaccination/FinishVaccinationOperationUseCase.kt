package com.example.gash.domain.usecase.vaccination

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.repository.VaccinationRepository
import com.example.gash.domain.transaction.TransactionRunner
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class FinishVaccinationOperationUseCase @Inject constructor(
    private val repository: VaccinationRepository,
    private val transactionRunner: TransactionRunner
) {
    suspend operator fun invoke(operationId: Long): Result<Unit> {
        return try {
            transactionRunner.run {
                val operation = repository.getOperation(operationId)
                if (operation == null || !operation.isOpen) throw DomainError.VaccinationOperationClosed

                if (repository.countRecords(operationId) == 0) {
                    repository.deleteOperation(operationId)
                } else {
                    repository.finishOperation(operationId)
                }
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: DomainError) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(DomainError.Unknown(e))
        }
    }
}