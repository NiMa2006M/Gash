package com.example.gash.domain.model

data class VaccinationOperation(
    val id: Long,
    val vaccineTypeId: Long,
    val vaccineTypeName: String,
    val injectionSite: InjectionSite,
    val doseMl: Double?,
    val herdId: Long?,
    val herdName: String?,
    val startedAt: Long,
    val finishedAt: Long?,
    val operatorName: String? = null
) {
    val isOpen: Boolean get() = finishedAt == null
}