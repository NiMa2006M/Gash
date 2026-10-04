package com.example.gash.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gash.core.database.dao.AnimalDao
import com.example.gash.core.database.dao.HerdDao
import com.example.gash.core.database.dao.RfidTagDao
import com.example.gash.core.database.dao.WeightRecordDao
import com.example.gash.core.database.entity.AnimalEntity
import com.example.gash.core.database.entity.HerdEntity
import com.example.gash.core.database.entity.RfidTagEntity
import com.example.gash.core.database.entity.WeightRecordEntity

@Database(
    entities = [
        HerdEntity::class,
        AnimalEntity::class,
        RfidTagEntity::class,
        WeightRecordEntity::class
    ],
    version = 4,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun herdDao(): HerdDao
    abstract fun animalDao(): AnimalDao
    abstract fun rfidTagDao(): RfidTagDao
    abstract fun weightRecordDao(): WeightRecordDao
}