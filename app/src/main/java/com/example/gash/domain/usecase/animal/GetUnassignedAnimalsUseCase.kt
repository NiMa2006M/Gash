package com.example.gash.domain.usecase.animal

import com.example.gash.domain.model.Animal
import com.example.gash.domain.repository.AnimalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUnassignedAnimalsUseCase @Inject constructor(
    private val repository: AnimalRepository
) {
    operator fun invoke(): Flow<List<Animal>> = repository.observeUnassignedAnimals()
}