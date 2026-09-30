package com.example.gash.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RfidTagAssignmentTest {

    @Test
    fun `create active RfidTagAssignment should hold correct initial values`() {
        // Given & When
        val assignment = RfidTagAssignment(
            id = 1L,
            code = "RFID-88123",
            animalId = 100L,
            isActive = true,
            assignedAt = 1700000000000L,
            unassignedAt = null
        )

        // Then
        assertEquals(1L, assignment.id)
        assertEquals("RFID-88123", assignment.code)
        assertEquals(100L, assignment.animalId)
        assertTrue(assignment.isActive)
        assertEquals(1700000000000L, assignment.assignedAt)
        assertNull(assignment.unassignedAt)
    }

    @Test
    fun `unassigning RfidTag should set isActive to false and record unassignedAt`() {
        // Given
        val activeAssignment = RfidTagAssignment(
            id = 1L,
            code = "RFID-88123",
            animalId = 100L,
            isActive = true,
            assignedAt = 1700000000000L,
            unassignedAt = null
        )

        // When
        val unassignTime = 1700005000000L
        val inactiveAssignment = activeAssignment.copy(
            isActive = false,
            unassignedAt = unassignTime
        )

        // Then
        assertTrue(activeAssignment.isActive)
        assertFalse(inactiveAssignment.isActive)
        assertEquals(unassignTime, inactiveAssignment.unassignedAt)
    }
}