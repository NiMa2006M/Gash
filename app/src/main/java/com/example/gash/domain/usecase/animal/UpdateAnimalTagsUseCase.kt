package com.example.gash.domain.usecase.animal

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.model.AnimalTags
import com.example.gash.domain.repository.AnimalRepository
import javax.inject.Inject

class UpdateAnimalTagsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    suspend operator fun invoke(animalId: Long, tags: AnimalTags): Result<Unit> {
        val animal = animalRepository.getAnimal(animalId)
            ?: return Result.failure(DomainError.AnimalNotFound)

        if (animal.expiredAt != null) {
            return Result.failure(DomainError.AnimalExpired)
        }

        animalRepository.updateAnimalTags(animalId, tags.normalized())
        return Result.success(Unit)
    }
}