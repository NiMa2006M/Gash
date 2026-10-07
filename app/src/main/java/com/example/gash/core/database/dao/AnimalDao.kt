package com.example.gash.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gash.core.database.entity.AnimalEntity
import kotlinx.coroutines.flow.Flow

data class AnimalListRow(
    @Embedded val animal: AnimalEntity,
    val activeRfidCode: String?
)

@Dao
interface AnimalDao {

    @Query("SELECT * FROM animals WHERE expiredAt IS NULL ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<AnimalEntity>>

    @Query(
        """
    SELECT animals.*, rfid_tags.code AS activeRfidCode
    FROM animals
    LEFT JOIN rfid_tags ON rfid_tags.animalId = animals.id AND rfid_tags.isActive = 1
    WHERE animals.id = :animalId
    """
    )
    fun observeByIdWithRfid(animalId: Long): Flow<AnimalListRow?>

    @Query(
        """
        SELECT animals.*, rfid_tags.code AS activeRfidCode
        FROM animals
        LEFT JOIN rfid_tags ON rfid_tags.animalId = animals.id AND rfid_tags.isActive = 1
        WHERE animals.herdId = :herdId AND animals.expiredAt IS NULL
        ORDER BY animals.createdAt DESC
        """
    )
    fun observeByHerdWithRfid(herdId: Long): Flow<List<AnimalListRow>>

    @Query("SELECT * FROM animals WHERE herdId IS NULL AND expiredAt IS NULL ORDER BY createdAt DESC")
    fun observeUnassigned(): Flow<List<AnimalEntity>>

    @Query("SELECT * FROM animals WHERE expiredAt IS NOT NULL ORDER BY expiredAt DESC")
    fun observeExpired(): Flow<List<AnimalEntity>>

    @Query("SELECT COUNT(*) FROM animals WHERE expiredAt IS NULL")
    fun observeTotalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM animals WHERE herdId = :herdId AND expiredAt IS NULL")
    fun observeCountByHerd(herdId: Long): Flow<Int>

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