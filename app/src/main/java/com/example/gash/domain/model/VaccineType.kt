package com.example.gash.domain.model

data class VaccineType(
    val id: Long,
    val name: String,
    val isArchived: Boolean = false
)