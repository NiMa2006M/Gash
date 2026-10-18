package com.example.gash.domain.repository

import com.example.gash.domain.model.VaccineType
import kotlinx.coroutines.flow.Flow

interface VaccineTypeRepository {
    fun observeActive(): Flow<List<VaccineType>>
    suspend fun findByName(name: String): VaccineType?
    suspend fun add(name: String): Long
    suspend fun rename(id: Long, name: String)
    suspend fun archive(id: Long)
    suspend fun restore(id: Long, name: String)
}