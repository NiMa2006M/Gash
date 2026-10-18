package com.example.gash.domain.usecase.vaccine

import com.example.gash.domain.repository.VaccineTypeRepository
import javax.inject.Inject

class ArchiveVaccineTypeUseCase @Inject constructor(
    private val repository: VaccineTypeRepository
) {
    suspend operator fun invoke(id: Long) = repository.archive(id)
}