package com.example.gash.data.repository

import com.example.gash.ui.helper.normalizedForSearch
import com.example.gash.core.database.dao.VaccineTypeDao
import com.example.gash.core.database.entity.VaccineTypeEntity
import com.example.gash.data.mapper.toDomain
import com.example.gash.domain.model.VaccineType
import com.example.gash.domain.repository.VaccineTypeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VaccineTypeRepositoryImpl @Inject constructor(
    private val dao: VaccineTypeDao
) : VaccineTypeRepository {

    override fun observeActive(): Flow<List<VaccineType>> =
        dao.observeActive().map { list -> list.map { it.toDomain() } }

    override suspend fun findByName(name: String): VaccineType? =
        dao.getByNormalizedName(name.normalizedForSearch())?.toDomain()

    override suspend fun add(name: String): Long =
        dao.insert(
            VaccineTypeEntity(
                name = name,
                normalizedName = name.normalizedForSearch(),
                createdAt = System.currentTimeMillis()
            )
        )

    override suspend fun rename(id: Long, name: String) {
        val entity = dao.getById(id) ?: return
        dao.update(entity.copy(name = name, normalizedName = name.normalizedForSearch()))
    }

    override suspend fun archive(id: Long) {
        val entity = dao.getById(id) ?: return
        dao.update(entity.copy(isArchived = true))
    }

    override suspend fun restore(id: Long, name: String) {
        val entity = dao.getById(id) ?: return
        dao.update(
            entity.copy(name = name, normalizedName = name.normalizedForSearch(), isArchived = false)
        )
    }
}