package com.example.gash.domain.model

data class Animal(
    val id: Long,
    val herdId: Long?,
    val createdAt: Long,
    val expiredAt: Long? = null,
    val activeRfidCode: String? = null,
    val tag1: String? = null,
    val tag2: String? = null,
    val tag3: String? = null
)