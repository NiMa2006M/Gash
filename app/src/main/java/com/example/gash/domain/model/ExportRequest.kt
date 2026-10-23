package com.example.gash.domain.model

sealed interface ExportRequest {
    data object AnimalsWeights : ExportRequest
    data class VaccinationOperation(val operationId: Long) : ExportRequest
}