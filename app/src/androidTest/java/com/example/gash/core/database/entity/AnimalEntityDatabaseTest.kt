package com.example.gash.core.database.entity

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gash.core.database.AppDatabase
import com.example.gash.core.database.entity.AnimalEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AnimalEntityDatabaseTest {

    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAnimalAndGetId() = runBlocking {
        // Given
        val animal = AnimalEntity(
            herdId = null,
            createdAt = System.currentTimeMillis(),
            expiredAt = null
        )

        // When
        val insertedId = db.animalDao().insert(animal)

        // Then
        val loadedAnimal = db.animalDao().getById(insertedId)
        assertEquals(insertedId, loadedAnimal?.id)
    }
}