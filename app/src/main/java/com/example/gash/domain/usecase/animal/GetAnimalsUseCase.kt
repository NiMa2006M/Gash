package com.example.gash.domain.usecase.animal

import com.example.gash.domain.model.Animal
import com.example.gash.domain.repository.AnimalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnimalsUseCase @Inject constructor(
    private val repository: AnimalRepository
) {
    operator fun invoke(herdId: Long? = null): Flow<List<Animal>> =
        if (herdId != null) repository.observeAnimalsByHerd(herdId)
        else repository.observeAnimals()
}