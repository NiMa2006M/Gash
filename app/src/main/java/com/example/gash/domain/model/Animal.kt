package com.example.gash.domain.model

data class Animal(
    val id: Long,
    val herdId: Long?,
    val createdAt: Long,
    val expiredAt: Long?,
    val activeRfidCode: String? = null
)
