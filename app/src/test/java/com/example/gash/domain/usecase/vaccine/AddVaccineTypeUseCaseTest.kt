package com.example.gash.domain.usecase.vaccine

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.model.VaccineType
import com.example.gash.domain.repository.VaccineTypeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddVaccineTypeUseCaseTest {

    private lateinit var repository: VaccineTypeRepository
    private lateinit var useCase: AddVaccineTypeUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = AddVaccineTypeUseCase(repository)
    }

    @Test
    fun `blank name fails`() = runTest {
        val result = useCase("   ")

        assertEquals(DomainError.EmptyVaccineName, result.exceptionOrNull())
    }

    @Test
    fun `existing active name is rejected as duplicate`() = runTest {
        coEvery { repository.findByName("Brucellosis") } returns VaccineType(1L, "Brucellosis")

        val result = useCase("Brucellosis")

        assertEquals(DomainError.DuplicateVaccineName, result.exceptionOrNull())
        coVerify(exactly = 0) { repository.add(any()) }
    }

    @Test
    fun `existing archived name is restored instead of duplicated`() = runTest {
        coEvery { repository.findByName("Brucellosis") } returns
                VaccineType(3L, "Brucellosis", isArchived = true)

        val result = useCase("Brucellosis")

        assertTrue(result.isSuccess)
        assertEquals(3L, result.getOrNull())
        coVerify { repository.restore(3L, "Brucellosis") }
        coVerify(exactly = 0) { repository.add(any()) }
    }

    @Test
    fun `new name is added`() = runTest {
        coEvery { repository.findByName("Anthrax") } returns null
        coEvery { repository.add("Anthrax") } returns 9L

        val result = useCase("  Anthrax ")

        assertEquals(9L, result.getOrNull())
    }
}