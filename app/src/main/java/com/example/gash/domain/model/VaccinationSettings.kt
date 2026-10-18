package com.example.gash.domain.model

data class VaccinationSettings(
    val vaccineTypeId: Long,
    val injectionSite: InjectionSite,
    val doseMl: Double?,
    val herdId: Long?,
    val herdName: String?,
    val operatorName: String? = null
) {
    fun normalized(): VaccinationSettings = copy(
        operatorName = operatorName
            ?.trim()
            ?.take(MAX_OPERATOR_NAME_LENGTH)
            ?.takeIf { it.isNotEmpty() }
    )

    companion object {
        const val MAX_OPERATOR_NAME_LENGTH = 40
    }
}