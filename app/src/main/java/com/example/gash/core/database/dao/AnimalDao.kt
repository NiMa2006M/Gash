package com.example.gash.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gash.core.database.entity.AnimalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao {

    @Query("SELECT * FROM animals WHERE expiredAt IS NULL ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<AnimalEntity>>

    @Query("SELECT * FROM animals WHERE herdId = :herdId AND expiredAt IS NULL ORDER BY createdAt DESC")
    fun observeByHerd(herdId: Long): Flow<List<AnimalEntity>>

    @Query("SELECT * FROM animals WHERE expiredAt IS NOT NULL ORDER BY expiredAt DESC")
    fun observeExpired(): Flow<List<AnimalEntity>>

    @Query("SELECT COUNT(*) FROM animals WHERE expiredAt IS NULL")
    fun observeTotalCount(): Flow<Int>

    @Query("SELECT * FROM animals WHERE id = :id")
    suspend fun getById(id: Long): AnimalEntity?

    @Insert
    suspend fun insert(animal: AnimalEntity): Long

    @Update
    suspend fun update(animal: AnimalEntity)

    @Query("UPDATE animals SET herdId = :herdId WHERE id = :animalId")
    suspend fun moveToHerd(animalId: Long, herdId: Long?)

    @Query("UPDATE animals SET expiredAt = :expiredAt WHERE id = :animalId")
    suspend fun setExpiredAt(animalId: Long, expiredAt: Long?)

    @Delete
    suspend fun delete(animal: AnimalEntity)
}