package com.example.gash.data.export

import com.example.gash.core.export.Spreadsheet
import com.example.gash.domain.model.ExportRequest

data class ExportDocument(
    val fileNamePrefix: String,
    val spreadsheet: Spreadsheet
)

interface ExportBuilder {
    fun canHandle(request: ExportRequest): Boolean
    suspend fun build(request: ExportRequest): ExportDocument
}