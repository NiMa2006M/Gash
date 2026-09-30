package com.example.gash.core.database.entity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AnimalEntityTest {

    @Test
    fun `create AnimalEntity with default id zero`() {
        // Given & When
        val entity = AnimalEntity(
            herdId = 5L,
            createdAt = 1700000000000L,
            expiredAt = null
        )

        // Then
        assertEquals(0L, entity.id)
        assertEquals(5L, entity.herdId)
        assertNull(entity.expiredAt)
    }
}