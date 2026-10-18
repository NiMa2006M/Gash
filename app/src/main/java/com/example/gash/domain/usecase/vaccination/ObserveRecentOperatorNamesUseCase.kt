package com.example.gash.domain.usecase.vaccination

import com.example.gash.domain.repository.VaccinationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRecentOperatorNamesUseCase @Inject constructor(
    private val repository: VaccinationRepository
) {
    operator fun invoke(): Flow<List<String>> = repository.observeRecentOperatorNames()
}