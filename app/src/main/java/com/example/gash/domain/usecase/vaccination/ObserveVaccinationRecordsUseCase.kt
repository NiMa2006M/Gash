package com.example.gash.domain.usecase.vaccination

import com.example.gash.domain.model.VaccinationRecord
import com.example.gash.domain.repository.VaccinationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveVaccinationRecordsUseCase @Inject constructor(
    private val repository: VaccinationRepository
) {
    operator fun invoke(operationId: Long): Flow<List<VaccinationRecord>> =
        repository.observeRecords(operationId)
}