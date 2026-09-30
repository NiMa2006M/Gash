package com.example.gash.domain.usecase.animal

import com.example.gash.domain.repository.AnimalRepository
import com.example.gash.domain.repository.RfidTagRepository
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before

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
            rfidTagRepository = rfidTagRepository
        )
    }

    @Test
    fun `register animal without RFID returns success`() = runTest {

        coEvery {
            animalRepository.registerAnimal(herdId = 10L)
        } returns 25L

        val result = registerAnimalUseCase(
            herdId = 10L,
            rfidCode = null
        )

        assertTrue(result.isSuccess)
        assertEquals(25L, result.getOrNull())
    }

    @Test
    fun `register animal with RFID assigns tag successfully`() = runTest {

        coEvery {
            animalRepository.registerAnimal(10L)
        } returns 25L

        coEvery {
            rfidTagRepository.assignTagToAnimal(
                code = "ABC123",
                animalId = 25L
            )
        } returns Result.success(Unit)

        val result = registerAnimalUseCase(
            herdId = 10L,
            rfidCode = "ABC123"
        )

        assertTrue(result.isSuccess)
        assertEquals(25L, result.getOrNull())
    }

//    @Test
//    fun `register animal fails when RFID assignment fails`() = runTest {
//
//        coEvery {
//            animalRepository.registerAnimal(10L)
//        } returns 25L
//
//        val error = IllegalStateException("RFID assignment failed")
//
//        coEvery {
//            rfidTagRepository.assignTagToAnimal(
//                code = "ABC123",
//                animalId = 25L
//            )
//        } returns Result.failure(error)
//
//        val result = registerAnimalUseCase(
//            herdId = 10L,
//            rfidCode = "ABC123"
//        )
//
//        assertTrue(result.isFailure)
//        assertEquals(error, result.exceptionOrNull())
//    }
}