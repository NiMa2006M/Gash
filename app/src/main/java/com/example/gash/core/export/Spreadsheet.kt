// Spreadsheet.kt
package com.example.gash.core.export

data class Sheet(
    val name: String,
    val headers: List<String>,
    val rows: List<List<Cell>>,
    val rightToLeft: Boolean = false
)

data class Spreadsheet(val sheets: List<Sheet>)