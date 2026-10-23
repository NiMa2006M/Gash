package com.example.gash.domain.model

data class VaccinationOperationSummary(
    val id: Long,
    val vaccineTypeName: String,
    val startedAt: Long,
    val finishedAt: Long?,
    val herdName: String?,
    val operatorName: String?,
    val recordCount: Int
) {
    val isOpen: Boolean get() = finishedAt == null
}