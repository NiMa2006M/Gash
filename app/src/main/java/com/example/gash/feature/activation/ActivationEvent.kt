package com.example.gash.feature.activation

sealed interface ActivationEvent {
    data object NavigateToHome : ActivationEvent
}