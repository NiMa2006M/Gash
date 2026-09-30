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
    is DomainError.Unknown -> UiText.StringResource(R.string.common_error_unknown)
}

fun Throwable.toUiText(): UiText =
    (this as? DomainError)?.toUiText() ?: UiText.StringResource(R.string.common_error_unknown)