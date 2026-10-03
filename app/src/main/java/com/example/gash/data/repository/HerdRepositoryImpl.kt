package com.example.gash.data.repository

import com.example.gash.core.database.dao.AnimalDao
import com.example.gash.core.database.dao.HerdDao
import com.example.gash.core.database.entity.HerdEntity
import com.example.gash.data.mapper.toDomain
import com.example.gash.domain.model.Herd
import com.example.gash.domain.repository.HerdRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class HerdRepositoryImpl @Inject constructor(
    private val herdDao: HerdDao,
    private val animalDao: AnimalDao
) : HerdRepository {

    override fun observeHerds(): Flow<List<Herd>> =
        combine(herdDao.observeAll(), animalDao.observeAll()) { herds, animals ->
            herds.map { herd ->
                val count = animals.count { it.herdId == herd.id }
                herd.toDomain(animalCount = count)
            }
        }

    override fun observeHerd(herdId: Long): Flow<Herd?> =
        combine(herdDao.observeById(herdId), animalDao.observeCountByHerd(herdId)) { entity, count ->
            entity?.toDomain(animalCount = count)
        }

    override suspend fun getHerd(id: Long): Herd? =
        herdDao.getById(id)?.toDomain()

    override suspend fun addHerd(name: String): Long =
        herdDao.insert(HerdEntity(name = name, createdAt = System.currentTimeMillis()))

    override suspend fun renameHerd(id: Long, newName: String) {
        val herd = herdDao.getById(id) ?: return
        herdDao.update(herd.copy(name = newName))
    }

    override suspend fun deleteHerd(id: Long) {
        herdDao.deleteById(id)
    }


}