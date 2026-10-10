package com.example.gash.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AnimalTagsTest {

    @Test
    fun `normalized trims values and turns blank into null`() {
        val result = AnimalTags(tag1 = "  A-1 ", tag2 = "   ", tag3 = "").normalized()

        assertEquals("A-1", result.tag1)
        assertNull(result.tag2)
        assertNull(result.tag3)
    }

    @Test
    fun `normalized caps value length`() {
        val result = AnimalTags(tag1 = "x".repeat(100)).normalized()

        assertEquals(AnimalTags.MAX_VALUE_LENGTH, result.tag1?.length)
    }

    @Test
    fun `withValue and valueFor work per slot and ignore invalid slot`() {
        val tags = AnimalTags().withValue(2, "B").withValue(9, "ignored")

        assertEquals("B", tags.valueFor(2))
        assertNull(tags.valueFor(1))
        assertNull(tags.valueFor(9))
    }
}