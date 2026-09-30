package com.example.gash.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AnimalTest {

    @Test
    fun `create Animal with default activeRfidCode should set it to null`() {
        // Given & When
        val animal = Animal(
            id = 100L,
            herdId = 50L,
            createdAt = 1700000000000L,
            expiredAt = null
        )

        // Then
        assertEquals(100L, animal.id)
        assertEquals(50L, animal.herdId)
        assertNull(animal.activeRfidCode)
    }

    @Test
    fun `create Animal with custom activeRfidCode and expiredAt`() {
        // Given & When
        val animal = Animal(
            id = 101L,
            herdId = null,
            createdAt = 1700000000000L,
            expiredAt = 1710000000000L,
            activeRfidCode = "RFID-12345"
        )

        // Then
        assertNull(animal.herdId)
        assertEquals("RFID-12345", animal.activeRfidCode)
        assertEquals(1710000000000L, animal.expiredAt)
    }
}