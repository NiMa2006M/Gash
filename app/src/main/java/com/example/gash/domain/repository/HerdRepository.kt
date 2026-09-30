package com.example.gash.domain.repository

import com.example.gash.domain.model.Herd
import kotlinx.coroutines.flow.Flow

interface HerdRepository {
    fun observeHerds(): Flow<List<Herd>>
    suspend fun getHerd(id: Long): Herd?
    suspend fun addHerd(name: String): Long
    suspend fun renameHerd(id: Long, newName: String)
    suspend fun deleteHerd(id: Long)
}