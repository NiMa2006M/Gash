package com.example.gash.di

import android.content.Context
import androidx.room.Room
import com.example.gash.core.database.AppDatabase
import com.example.gash.core.database.dao.AnimalDao
import com.example.gash.core.database.dao.HerdDao
import com.example.gash.core.database.dao.RfidTagDao
import com.example.gash.core.database.dao.WeightRecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "gash.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideHerdDao(db: AppDatabase): HerdDao = db.herdDao()

    @Provides
    fun provideAnimalDao(db: AppDatabase): AnimalDao = db.animalDao()

    @Provides
    fun provideRfidTagDao(db: AppDatabase): RfidTagDao = db.rfidTagDao()

    @Provides
    fun provideWeightRecordDao(db: AppDatabase): WeightRecordDao = db.weightRecordDao()
}