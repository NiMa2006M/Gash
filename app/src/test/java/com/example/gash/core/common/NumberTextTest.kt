package com.example.gash.core.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NumberTextTest {

    @Test
    fun `persian digits and decimal separator are parsed`() {
        assertEquals(2.5, "\u06F2\u066B\u06F5".toDecimalOrNull()!!, 0.0)   // ۲٫۵
    }

    @Test
    fun `arabic digits and comma are parsed`() {
        assertEquals(12.5, "\u0661\u0662,\u0665".toDecimalOrNull()!!, 0.0)  // ١٢,٥
    }

    @Test
    fun `plain ascii number is parsed`() {
        assertEquals(3.0, "3".toDecimalOrNull()!!, 0.0)
    }

    @Test
    fun `garbage returns null`() {
        assertNull("abc".toDecimalOrNull())
        assertNull("".toDecimalOrNull())
    }
}