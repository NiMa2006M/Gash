package com.example.gash.domain.usecase.vaccination

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.model.Animal
import com.example.gash.domain.model.InjectionSite
import com.example.gash.domain.model.RfidTagAssignment
import com.example.gash.domain.model.VaccinationOperation
import com.example.gash.domain.model.VaccinationSettings
import com.example.gash.domain.repository.AnimalRepository
import com.example.gash.domain.repository.RfidTagRepository
import com.example.gash.domain.repository.VaccinationRepository
import com.example.gash.domain.transaction.TransactionRunner
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private object ImmediateTransactionRunner : TransactionRunner {
    override suspend fun <T> run(block: suspend () -> T): T = block()
}

class RegisterVaccinationUseCaseTest {

    private lateinit var vaccinationRepository: VaccinationRepository
    private lateinit var rfidTagRepository: RfidTagRepository
    private lateinit var animalRepository: AnimalRepository
    private lateinit var useCase: RegisterVaccinationUseCase

    private fun settings(herdId: Long? = null) = VaccinationSettings(
        vaccineTypeId = 1L,
        injectionSite = InjectionSite.INTRAMUSCULAR,
        doseMl = null,
        herdId = herdId,
        herdName = herdId?.let { "Herd" }
    )

    private fun tag(animalId: Long) = RfidTagAssignment(
        id = 1L, code = "ABC", animalId = animalId, isActive = true, assignedAt = 0L, unassignedAt = null
    )

    private fun animal(id: Long = 25L, herdId: Long? = null, expiredAt: Long? = null) =
        Animal(id = id, herdId = herdId, createdAt = 0L, expiredAt = expiredAt)

    private fun operation(id: Long, herdId: Long? = null, finishedAt: Long? = null) = VaccinationOperation(
        id = id, vaccineTypeId = 1L, vaccineTypeName = "V", injectionSite = InjectionSite.INTRAMUSCULAR,
        doseMl = null, herdId = herdId, herdName = null, startedAt = 0L, finishedAt = finishedAt
    )

    @Before
    fun setup() {
        vaccinationRepository = mockk()
        rfidTagRepository = mockk()
        animalRepository = mockk()
        useCase = RegisterVaccinationUseCase(
            vaccinationRepository, rfidTagRepository, animalRepository, ImmediateTransactionRunner
        )
    }

    private fun happyPathStubs(operationId: Long = 7L) {
        coEvery { rfidTagRepository.findActiveByCode("ABC") } returns tag(25L)
        coEvery { animalRepository.getAnimal(25L) } returns animal()
        coEvery { vaccinationRepository.createOperation(any()) } returns operationId
        coEvery { vaccinationRepository.hasRecord(operationId, 25L) } returns false
        coEvery { vaccinationRepository.addRecord(operationId, 25L, "ABC") } returns 1L
        coEvery { vaccinationRepository.getOperation(operationId) } returns operation(operationId)
    }

    @Test
    fun `first registration creates the operation`() = runTest {
        happyPathStubs()

        val result = useCase(operationId = null, settings = settings(), rfidCode = " ABC ")

        assertTrue(result.isSuccess)
        assertEquals(7L, result.getOrNull()?.operation?.id)
        assertEquals(25L, result.getOrNull()?.animalId)
        coVerify(exactly = 1) { vaccinationRepository.createOperation(any()) }
    }

    @Test
    fun `unknown rfid fails and does not create an operation`() = runTest {
        coEvery { rfidTagRepository.findActiveByCode("ABC") } returns null

        val result = useCase(operationId = null, settings = settings(), rfidCode = "ABC")

        assertEquals(DomainError.RfidTagNotFound, result.exceptionOrNull())
        coVerify(exactly = 0) { vaccinationRepository.createOperation(any()) }
    }

    @Test
    fun `expired animal is rejected`() = runTest {
        coEvery { rfidTagRepository.findActiveByCode("ABC") } returns tag(25L)
        coEvery { animalRepository.getAnimal(25L) } returns animal(expiredAt = 100L)

        val result = useCase(operationId = null, settings = settings(), rfidCode = "ABC")

        assertEquals(DomainError.AnimalExpired, result.exceptionOrNull())
    }

    @Test
    fun `animal outside selected herd asks for confirmation`() = runTest {
        coEvery { rfidTagRepository.findActiveByCode("ABC") } returns tag(25L)
        coEvery { animalRepository.getAnimal(25L) } returns animal(herdId = 99L)

        val result = useCase(operationId = null, settings = settings(herdId = 5L), rfidCode = "ABC")

        val error = result.exceptionOrNull()
        assertTrue(error is DomainError.AnimalNotInSelectedHerd)
        assertEquals(25L, (error as DomainError.AnimalNotInSelectedHerd).animalId)
        coVerify(exactly = 0) { vaccinationRepository.createOperation(any()) }
    }

    @Test
    fun `animal outside selected herd is registered after confirmation`() = runTest {
        happyPathStubs()
        coEvery { animalRepository.getAnimal(25L) } returns animal(herdId = 99L)

        val result = useCase(
            operationId = null,
            settings = settings(herdId = 5L),
            rfidCode = "ABC",
            allowHerdMismatch = true
        )

        assertTrue(result.isSuccess)
    }

    @Test
    fun `same animal twice in one operation is rejected`() = runTest {
        coEvery { vaccinationRepository.getOperation(7L) } returns operation(7L)
        coEvery { rfidTagRepository.findActiveByCode("ABC") } returns tag(25L)
        coEvery { animalRepository.getAnimal(25L) } returns animal()
        coEvery { vaccinationRepository.hasRecord(7L, 25L) } returns true

        val result = useCase(operationId = 7L, settings = settings(), rfidCode = "ABC")

        assertEquals(DomainError.AnimalAlreadyVaccinatedInOperation, result.exceptionOrNull())
    }

    @Test
    fun `finished operation cannot receive new records`() = runTest {
        coEvery { vaccinationRepository.getOperation(7L) } returns operation(7L, finishedAt = 500L)

        val result = useCase(operationId = 7L, settings = settings(), rfidCode = "ABC")

        assertEquals(DomainError.VaccinationOperationClosed, result.exceptionOrNull())
    }

    @Test
    fun `non positive dose is rejected before touching the database`() = runTest {
        val result = useCase(
            operationId = null,
            settings = settings().copy(doseMl = 0.0),
            rfidCode = "ABC"
        )

        assertEquals(DomainError.InvalidDose, result.exceptionOrNull())
    }
}