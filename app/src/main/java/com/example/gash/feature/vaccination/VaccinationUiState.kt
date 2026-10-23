package com.example.gash.feature.vaccination

import com.example.gash.core.common.toDecimalOrNull
import com.example.gash.domain.model.Herd
import com.example.gash.domain.model.InjectionSite
import com.example.gash.domain.model.VaccinationOperation
import com.example.gash.domain.model.VaccinationRecord
import com.example.gash.domain.model.VaccinationSettings
import com.example.gash.domain.model.VaccineType
import com.example.gash.ui.helper.UiText

enum class VaccinationTab { REGISTER, SETTINGS }

enum class SettingsPicker { VACCINE_TYPE, HERD }

sealed interface RegisterFeedback {
    data class Success(val rfidCode: String) : RegisterFeedback
    data class Failure(val message: UiText) : RegisterFeedback
}

data class HerdMismatchPrompt(val rfidCode: String, val animalId: Long)

sealed interface VaccinationEvent {
    data object Finished : VaccinationEvent
}

data class VaccinationUiState(
    val isLoading: Boolean = true,
    val selectedTab: VaccinationTab = VaccinationTab.SETTINGS,

    val operation: VaccinationOperation? = null,

    val vaccineType: VaccineType? = null,
    val injectionSite: InjectionSite? = null,
    val doseText: String = "",
    val herdId: Long? = null,
    val herdName: String? = null,
    val herdAnimalCount: Int? = null,

    val availableVaccineTypes: List<VaccineType> = emptyList(),
    val availableHerds: List<Herd> = emptyList(),
    val picker: SettingsPicker? = null,

    val rfidText: String = "",
    val records: List<VaccinationRecord> = emptyList(),
    val feedback: RegisterFeedback? = null,
    val herdMismatchPrompt: HerdMismatchPrompt? = null,
    val recordPendingDelete: VaccinationRecord? = null,
    val isFinishConfirmOpen: Boolean = false,
    val isSubmitting: Boolean = false,

    val operatorName: String = "",
    val recentOperatorNames: List<String> = emptyList(),
) {
    val isSettingsLocked: Boolean get() = operation != null

    val doseValue: Double? get() = doseText.toDecimalOrNull()

    val isDoseValid: Boolean
        get() = doseText.isBlank() || (doseValue?.let { it > 0.0 } == true)

    val currentSettings: VaccinationSettings?
        get() {
            val type = vaccineType ?: return null
            val site = injectionSite ?: return null
            if (!isDoseValid) return null
            return VaccinationSettings(
                vaccineTypeId = type.id,
                injectionSite = site,
                doseMl = if (doseText.isBlank()) null else doseValue,
                herdId = herdId,
                herdName = herdName,
                operatorName = operatorName.ifBlank { null }
            )
        }

    val canRegister: Boolean get() = operation != null || currentSettings != null
}