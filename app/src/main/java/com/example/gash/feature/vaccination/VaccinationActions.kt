// VaccinationActions.kt
package com.example.gash.feature.vaccination

import com.example.gash.domain.model.InjectionSite
import com.example.gash.domain.model.VaccinationRecord
import com.example.gash.domain.model.VaccineType

class VaccinationActions(
    val onBack: () -> Unit,
    val onTabSelected: (VaccinationTab) -> Unit,
    val onOpenPicker: (SettingsPicker) -> Unit,
    val onDismissPicker: () -> Unit,
    val onVaccineTypeSelected: (VaccineType) -> Unit,
    val onHerdSelected: (herdId: Long?, herdName: String?) -> Unit,
    val onInjectionSiteSelected: (InjectionSite) -> Unit,
    val onDoseChange: (String) -> Unit,
    val onOperatorNameChange: (String) -> Unit,
    val onContinueClick: () -> Unit,
    val onGoToVaccineSettings: () -> Unit,
    val onRfidChange: (String) -> Unit,
    val onRegisterClick: () -> Unit,
    val onConfirmHerdMismatch: () -> Unit,
    val onDismissHerdMismatch: () -> Unit,
    val onRecordClick: (VaccinationRecord) -> Unit,
    val onRecordDeleteClick: (VaccinationRecord) -> Unit,
    val onConfirmDeleteRecord: () -> Unit,
    val onDismissDeleteRecord: () -> Unit,
    val onFinishClick: () -> Unit,
    val onConfirmFinish: () -> Unit,
    val onDismissFinish: () -> Unit
)