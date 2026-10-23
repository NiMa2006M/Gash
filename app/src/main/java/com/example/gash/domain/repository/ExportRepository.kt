package com.example.gash.domain.repository

import com.example.gash.domain.model.ExportFile
import com.example.gash.domain.model.ExportRequest

interface ExportRepository {
    suspend fun createExportFile(request: ExportRequest): ExportFile
}