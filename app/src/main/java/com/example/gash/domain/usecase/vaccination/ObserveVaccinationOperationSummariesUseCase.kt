

package com.example.gash.domain.usecase.vaccination

import com.example.gash.domain.model.VaccinationOperationSummary
import com.example.gash.domain.repository.VaccinationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveVaccinationOperationSummariesUseCase @Inject constructor(
    private val repository: VaccinationRepository
) {
    operator fun invoke(): Flow<List<VaccinationOperationSummary>> = repository.observeOperationSummaries()
}