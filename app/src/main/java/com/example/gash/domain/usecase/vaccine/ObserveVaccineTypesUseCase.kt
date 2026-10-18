package com.example.gash.domain.usecase.vaccine

import com.example.gash.domain.model.VaccineType
import com.example.gash.domain.repository.VaccineTypeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveVaccineTypesUseCase @Inject constructor(
    private val repository: VaccineTypeRepository
) {
    operator fun invoke(): Flow<List<VaccineType>> = repository.observeActive()
}