package com.example.gash.domain.usecase.animal

import com.example.gash.domain.repository.AnimalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnimalCountUseCase @Inject constructor(
    private val repository: AnimalRepository
) {
    operator fun invoke(): Flow<Int> = repository.observeTotalCount()
}