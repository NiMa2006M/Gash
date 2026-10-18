package com.example.gash.domain.usecase.vaccination

import com.example.gash.domain.model.VaccinationOperation
import com.example.gash.domain.repository.VaccinationRepository
import javax.inject.Inject

class GetOpenVaccinationOperationUseCase @Inject constructor(
    private val repository: VaccinationRepository
) {
    suspend operator fun invoke(): VaccinationOperation? = repository.getOpenOperation()
}