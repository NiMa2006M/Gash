package com.example.gash.domain.repository

import com.example.gash.domain.model.Animal
import kotlinx.coroutines.flow.Flow

interface AnimalRepository {
    fun observeAnimals(): Flow<List<Animal>>
    fun observeAnimalsByHerd(herdId: Long): Flow<List<Animal>>
    fun observeExpiredAnimals(): Flow<List<Animal>>
    fun observeTotalCount(): Flow<Int>
    suspend fun getAnimal(id: Long): Animal?
    suspend fun registerAnimal(herdId: Long?): Long
    suspend fun moveToHerd(animalId: Long, herdId: Long?)
    suspend fun expireAnimal(animalId: Long, expiredAt: Long = System.currentTimeMillis())
    suspend fun restoreAnimal(animalId: Long)
}