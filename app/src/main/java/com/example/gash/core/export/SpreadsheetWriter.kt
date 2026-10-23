// SpreadsheetWriter.kt
package com.example.gash.core.export

import java.io.OutputStream

interface SpreadsheetWriter {
    fun write(spreadsheet: Spreadsheet, output: OutputStream)
}