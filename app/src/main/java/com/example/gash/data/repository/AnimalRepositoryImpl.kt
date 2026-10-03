package com.example.gash.data.repository

import com.example.gash.core.database.dao.AnimalDao
import com.example.gash.core.database.dao.RfidTagDao
import com.example.gash.core.database.entity.AnimalEntity
import com.example.gash.data.mapper.toDomain
import com.example.gash.domain.model.Animal
import com.example.gash.domain.repository.AnimalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnimalRepositoryImpl @Inject constructor(
    private val animalDao: AnimalDao,
    private val rfidTagDao: RfidTagDao
) : AnimalRepository {

    override fun observeAnimals(): Flow<List<Animal>> =
        animalDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeAnimalsByHerd(herdId: Long): Flow<List<Animal>> =
        animalDao.observeByHerdWithRfid(herdId).map { rows ->
            rows.map { row -> row.animal.toDomain(activeRfidCode = row.activeRfidCode) }
        }

    override fun observeUnassignedAnimals(): Flow<List<Animal>> =
        animalDao.observeUnassigned().map { list -> list.map { it.toDomain() } }

    override fun observeExpiredAnimals(): Flow<List<Animal>> =
        animalDao.observeExpired().map { list -> list.map { it.toDomain() } }

    override fun observeTotalCount(): Flow<Int> = animalDao.observeTotalCount()

    override suspend fun getAnimal(id: Long): Animal? {
        val entity = animalDao.getById(id) ?: return null
        val activeTag = rfidTagDao.getActiveByAnimal(id)
        return entity.toDomain(activeRfidCode = activeTag?.code)
    }

    override suspend fun registerAnimal(herdId: Long?): Long =
        animalDao.insert(AnimalEntity(herdId = herdId, createdAt = System.currentTimeMillis(), expiredAt = null))

    override suspend fun moveToHerd(animalId: Long, herdId: Long?) {
        animalDao.moveToHerd(animalId, herdId)
    }

    override suspend fun expireAnimal(animalId: Long, expiredAt: Long) {
        animalDao.setExpiredAt(animalId, expiredAt)
        val activeTag = rfidTagDao.getActiveByAnimal(animalId)
        if (activeTag != null) {
            rfidTagDao.deactivate(activeTag.id, expiredAt)
        }
    }

    override suspend fun restoreAnimal(animalId: Long) {
        animalDao.setExpiredAt(animalId, null)
    }
}