package com.example.gash.domain.usecase.export

import com.example.gash.domain.error.DomainError
import com.example.gash.domain.model.ExportFile
import com.example.gash.domain.model.ExportRequest
import com.example.gash.domain.repository.ExportRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class CreateExportFileUseCase @Inject constructor(
    private val repository: ExportRepository
) {
    suspend operator fun invoke(request: ExportRequest): Result<ExportFile> = try {
        Result.success(repository.createExportFile(request))
    } catch (e: CancellationException) {
        throw e
    } catch (e: DomainError) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(DomainError.ExportFailed(e))
    }
}