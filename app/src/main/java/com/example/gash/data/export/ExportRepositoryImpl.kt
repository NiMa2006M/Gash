package com.example.gash.data.export

import com.example.gash.core.export.SpreadsheetWriter
import com.example.gash.domain.model.ExportFile
import com.example.gash.domain.model.ExportRequest
import com.example.gash.domain.repository.ExportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExportRepositoryImpl @Inject constructor(
    private val builders: Set<@JvmSuppressWildcards ExportBuilder>,
    private val writer: SpreadsheetWriter,
    private val storage: ExportFileStorage
) : ExportRepository {

    override suspend fun createExportFile(request: ExportRequest): ExportFile =
        withContext(Dispatchers.IO) {
            val builder = builders.firstOrNull { it.canHandle(request) }
                ?: error("No ExportBuilder registered for $request")
            val document = builder.build(request)
            storage.write(document.fileNamePrefix, document.spreadsheet, writer)
        }
}