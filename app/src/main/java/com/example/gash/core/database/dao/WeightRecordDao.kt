package com.example.gash.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gash.core.database.entity.WeightRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightRecordDao {

    @Query("SELECT * FROM weight_records WHERE animalId = :animalId ORDER BY recordedAt DESC")
    fun observeHistory(animalId: Long): Flow<List<WeightRecordEntity>>

    @Query("SELECT * FROM weight_records WHERE animalId = :animalId ORDER BY recordedAt DESC LIMIT 1")
    suspend fun getLatest(animalId: Long): WeightRecordEntity?

    @Insert
    suspend fun insert(record: WeightRecordEntity): Long
}