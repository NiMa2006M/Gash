package com.example.gash.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gash.core.database.entity.VaccineTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccineTypeDao {

    @Query("SELECT * FROM vaccine_types WHERE isArchived = 0 ORDER BY name COLLATE NOCASE ASC")
    fun observeActive(): Flow<List<VaccineTypeEntity>>

    @Query("SELECT * FROM vaccine_types WHERE id = :id")
    suspend fun getById(id: Long): VaccineTypeEntity?

    @Query("SELECT * FROM vaccine_types WHERE normalizedName = :normalizedName LIMIT 1")
    suspend fun getByNormalizedName(normalizedName: String): VaccineTypeEntity?

    @Insert
    suspend fun insert(entity: VaccineTypeEntity): Long

    @Update
    suspend fun update(entity: VaccineTypeEntity)
}