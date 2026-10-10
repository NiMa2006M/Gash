package com.example.gash.ui.helper

fun String.normalizedForSearch(): String = buildString(length) {
    for (ch in this@normalizedForSearch.trim()) {
        append(
            when (ch) {
                '\u064A', '\u0649' -> '\u06CC'
                '\u0643' -> '\u06A9'
                in '\u0660'..'\u0669' -> '0' + (ch - '\u0660')
                in '\u06F0'..'\u06F9' -> '0' + (ch - '\u06F0')
                else -> ch.lowercaseChar()
            }
        )
    }
}