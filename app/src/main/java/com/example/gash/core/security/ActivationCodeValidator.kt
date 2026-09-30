package com.example.gash.core.security

interface ActivationCodeValidator {
    fun validate(deviceId: String, enteredCode: String): Boolean
}