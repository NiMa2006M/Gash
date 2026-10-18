package com.example.gash.core.common

fun String.toDecimalOrNull(): Double? {
    val normalized = buildString {
        for (ch in this@toDecimalOrNull.trim()) {
            append(
                when (ch) {
                    in '\u0660'..'\u0669' -> '0' + (ch - '\u0660')
                    in '\u06F0'..'\u06F9' -> '0' + (ch - '\u06F0')
                    '\u066B', '\u060C', ',' -> '.'
                    else -> ch
                }
            )
        }
    }
    return normalized.toDoubleOrNull()
}