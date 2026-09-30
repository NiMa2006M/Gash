package com.example.gash.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gash.core.database.entity.HerdEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HerdDao {

    @Query("SELECT * FROM herds ORDER BY name ASC")
    fun observeAll(): Flow<List<HerdEntity>>

    @Query("SELECT * FROM herds WHERE id = :id")
    suspend fun getById(id: Long): HerdEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(herd: HerdEntity): Long

    @Update
    suspend fun update(herd: HerdEntity)

    @Delete
    suspend fun delete(herd: HerdEntity)

    @Query("DELETE FROM herds WHERE id = :id")
    suspend fun deleteById(id: Long)
}