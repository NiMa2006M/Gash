package com.example.gash.domain.usecase.animal

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.model.AnimalTags
import com.example.gash.domain.repository.AnimalRepository
import com.example.gash.domain.repository.RfidTagRepository
import com.example.gash.domain.transaction.TransactionRunner
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private object ImmediateTransactionRunner : TransactionRunner {
    override suspend fun <T> run(block: suspend () -> T): T = block()
}

class RegisterAnimalUseCaseTest {

    private lateinit var animalRepository: AnimalRepository
    private lateinit var rfidTagRepository: RfidTagRepository
    private lateinit var registerAnimalUseCase: RegisterAnimalUseCase

    @Before
    fun setup() {
        animalRepository = mockk()
        rfidTagRepository = mockk()

        registerAnimalUseCase = RegisterAnimalUseCase(
            animalRepository = animalRepository,
            rfidTagRepository = rfidTagRepository,
            transactionRunner = ImmediateTransactionRunner
        )
    }

    @Test
    fun `register animal without RFID returns success`() = runTest {
        coEvery { animalRepository.registerAnimal(herdId = 10L) } returns 25L

        val result = registerAnimalUseCase(herdId = 10L, rfidCode = null)

        assertTrue(result.isSuccess)
        assertEquals(25L, result.getOrNull())
    }

    @Test
    fun `register animal with RFID assigns tag successfully`() = runTest {
        coEvery { animalRepository.registerAnimal(10L) } returns 25L
        coEvery {
            rfidTagRepository.assignTagToAnimal(code = "ABC123", animalId = 25L)
        } returns Result.success(Unit)

        val result = registerAnimalUseCase(herdId = 10L, rfidCode = "ABC123")

        assertTrue(result.isSuccess)
        assertEquals(25L, result.getOrNull())
    }

    @Test
    fun `register animal fails with typed error when RFID is active on another animal`() = runTest {
        coEvery { animalRepository.registerAnimal(10L) } returns 25L
        coEvery {
            rfidTagRepository.assignTagToAnimal(code = "ABC123", animalId = 25L)
        } returns Result.failure(DomainError.RfidCodeActiveOnAnotherAnimal)

        val result = registerAnimalUseCase(herdId = 10L, rfidCode = "ABC123")

        assertTrue(result.isFailure)
        assertEquals(DomainError.RfidCodeActiveOnAnotherAnimal, result.exceptionOrNull())
    }

    @Test
    fun `register animal passes normalized tags to repository`() = runTest {
        coEvery {
            animalRepository.registerAnimal(10L, AnimalTags(tag1 = "A-1"))
        } returns 25L

        val result = registerAnimalUseCase(
            herdId = 10L,
            rfidCode = null,
            tags = AnimalTags(tag1 = "  A-1  ", tag2 = "   ")
        )

        assertTrue(result.isSuccess)
        assertEquals(25L, result.getOrNull())
    }
}