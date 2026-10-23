// ExportFileStorage.kt
package com.example.gash.data.export

import android.content.Context
import com.example.gash.core.export.Spreadsheet
import com.example.gash.core.export.SpreadsheetWriter
import com.example.gash.domain.model.ExportFile
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ExportFileStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun write(fileNamePrefix: String, spreadsheet: Spreadsheet, writer: SpreadsheetWriter): ExportFile {
        val directory = File(context.cacheDir, EXPORT_DIR).apply { mkdirs() }
        deleteOldFiles(directory)

        val safePrefix = fileNamePrefix.replace(UNSAFE_FILE_CHARS, "_")
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val file = File(directory, "${safePrefix}_$stamp.xlsx")

        FileOutputStream(file).use { output -> writer.write(spreadsheet, output) }

        return ExportFile(fileName = file.name, filePath = file.absolutePath, mimeType = XLSX_MIME_TYPE)
    }

    private fun deleteOldFiles(directory: File) {
        val threshold = System.currentTimeMillis() - MAX_FILE_AGE_MILLIS
        directory.listFiles()?.filter { it.lastModified() < threshold }?.forEach { it.delete() }
    }

    private companion object {
        const val EXPORT_DIR = "exports"
        const val XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        const val MAX_FILE_AGE_MILLIS = 24L * 60 * 60 * 1000
        val UNSAFE_FILE_CHARS = Regex("[^A-Za-z0-9._-]")
    }
}