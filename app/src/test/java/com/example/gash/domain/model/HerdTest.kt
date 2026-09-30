package com.example.gash.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class HerdTest {

    @Test
    fun `create Herd with default animalCount should set it to zero`() {
        // Given & When
        val herd = Herd(
            id = 1L,
            name = "my herd",
            createdAt = 1700000000000L
        )

        // Then
        assertEquals(1L, herd.id)
        assertEquals("my herd", herd.name)
        assertEquals(0, herd.animalCount)
    }

    @Test
    fun `create Herd with custom animalCount`() {
        // Given & When
        val herd = Herd(
            id = 2L,
            name = "pro herd",
            createdAt = 1700000000000L,
            animalCount = 45
        )

        // Then
        assertEquals(45, herd.animalCount)
    }

    @Test
    fun `updating animalCount using copy method should create new instance with updated count`() {
        // Given
        val initialHerd = Herd(
            id = 3L,
            name = "Herd 3",
            createdAt = 1700000000000L,
            animalCount = 10
        )

        // When
        val updatedHerd = initialHerd.copy(animalCount = initialHerd.animalCount + 1)

        // Then
        assertEquals(10, initialHerd.animalCount)
        assertEquals(11, updatedHerd.animalCount)
    }
}