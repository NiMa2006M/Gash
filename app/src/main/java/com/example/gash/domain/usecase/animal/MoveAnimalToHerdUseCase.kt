package com.example.gash.domain.usecase.animal

import com.example.gash.domain.repository.AnimalRepository
import javax.inject.Inject

class MoveAnimalToHerdUseCase @Inject constructor(
    private val repository: AnimalRepository
) {
    suspend operator fun invoke(animalId: Long, newHerdId: Long?) {
        repository.moveToHerd(animalId, newHerdId)
    }
}