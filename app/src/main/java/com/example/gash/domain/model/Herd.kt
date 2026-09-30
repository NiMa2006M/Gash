package com.example.gash.domain.model

data class Herd(
    val id: Long,
    val name: String,
    val createdAt: Long,
    val animalCount: Int = 0
)