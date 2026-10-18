package com.example.gash.ui.helper

import com.example.gash.R
import com.example.gash.domain.error.DomainError

fun DomainError.toUiText(): UiText = when (this) {
    is DomainError.InvalidActivationCode -> UiText.StringResource(R.string.activation_code_wrongCode)
    is DomainError.EmptyRfidCode -> UiText.StringResource(R.string.error_empty_rfid_code)
    is DomainError.EmptyHerdName -> UiText.StringResource(R.string.error_empty_herd_name)
    is DomainError.RfidAssignmentFailed -> UiText.StringResource(R.string.error_rfid_assignment_failed)
    is DomainError.AnimalNotFound -> UiText.StringResource(R.string.error_animal_not_found)
    is DomainError.AnimalExpired -> UiText.StringResource(R.string.error_animal_expired)
    is DomainError.InvalidWeight -> UiText.StringResource(R.string.error_invalid_weight)
    is DomainError.RfidCodeActiveOnAnotherAnimal -> UiText.StringResource(R.string.error_rfid_code_active_on_another_animal)
    is DomainError.InvalidFarmInfo -> UiText.StringResource(R.string.common_error_required_field)
    is DomainError.ProfileImageFailed -> UiText.StringResource(R.string.error_profile_image_failed)
    is DomainError.EmptyVaccineName -> UiText.StringResource(R.string.error_empty_vaccine_name)
    is DomainError.DuplicateVaccineName -> UiText.StringResource(R.string.error_duplicate_vaccine_name)
    is DomainError.InvalidDose -> UiText.StringResource(R.string.error_invalid_dose)
    is DomainError.RfidTagNotFound -> UiText.StringResource(R.string.error_rfid_tag_not_found)
    is DomainError.AnimalNotInSelectedHerd -> UiText.StringResource(R.string.error_animal_not_in_selected_herd)
    is DomainError.AnimalAlreadyVaccinatedInOperation -> UiText.StringResource(R.string.error_animal_already_vaccinated)
    is DomainError.VaccinationOperationClosed -> UiText.StringResource(R.string.error_vaccination_operation_closed)
    is DomainError.VaccinationRecordNotFound -> UiText.StringResource(R.string.error_vaccination_record_not_found)
    is DomainError.Unknown -> UiText.StringResource(R.string.common_error_unknown)
}

fun Throwable.toUiText(): UiText =
    (this as? DomainError)?.toUiText() ?: UiText.StringResource(R.string.common_error_unknown)