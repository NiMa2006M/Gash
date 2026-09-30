package com.example.gash.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gash.core.database.entity.RfidTagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RfidTagDao {

    @Query(""" SELECT * FROM rfid_tags WHERE code = :code AND isActive = 1 LIMIT 1 """)
    suspend fun getActiveByCode(code: String): RfidTagEntity?

    @Query(""" SELECT * FROM rfid_tags WHERE animalId = :animalId AND isActive = 1 LIMIT 1 """)
    suspend fun getActiveByAnimal(animalId: Long): RfidTagEntity?

    @Query(""" SELECT * FROM rfid_tags WHERE animalId = :animalId ORDER BY assignedAt DESC """)
    fun observeHistoryByAnimal(animalId: Long): Flow<List<RfidTagEntity>>

    @Insert
    suspend fun insert(tag: RfidTagEntity): Long

    @Update
    suspend fun update(tag: RfidTagEntity)

    @Query(""" UPDATE rfid_tags SET isActive = 0, unassignedAt = :unassignedAt WHERE id = :id """)
    suspend fun deactivate(id: Long, unassignedAt: Long)
}