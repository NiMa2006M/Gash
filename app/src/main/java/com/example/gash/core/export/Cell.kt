package com.example.gash.core.export

sealed interface Cell {
    data class Text(val value: String) : Cell
    data class Number(val value: Double) : Cell
    data object Empty : Cell

    companion object {
        fun of(value: String?): Cell = if (value.isNullOrBlank()) Empty else Text(value)
        fun of(value: Long?): Cell = value?.let { Number(it.toDouble()) } ?: Empty
        fun of(value: Double?): Cell = value?.let { Number(it) } ?: Empty
    }
}