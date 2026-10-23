package com.example.gash.core.export

import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.Writer
import java.math.BigDecimal
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject
import kotlin.math.abs

class XlsxSpreadsheetWriter @Inject constructor() : SpreadsheetWriter {

    override fun write(spreadsheet: Spreadsheet, output: OutputStream) {
        require(spreadsheet.sheets.isNotEmpty()) { "Spreadsheet needs at least one sheet" }

        val sheetNames = uniqueSheetNames(spreadsheet.sheets)
        val zip = ZipOutputStream(output)

        zip.entry("[Content_Types].xml") { writeContentTypes(spreadsheet.sheets.size) }
        zip.entry("_rels/.rels") { writeRootRels() }
        zip.entry("xl/workbook.xml") { writeWorkbook(sheetNames) }
        zip.entry("xl/_rels/workbook.xml.rels") { writeWorkbookRels(sheetNames.size) }
        zip.entry("xl/styles.xml") { writeStyles() }
        spreadsheet.sheets.forEachIndexed { index, sheet ->
            zip.entry("xl/worksheets/sheet${index + 1}.xml") { writeSheet(sheet) }
        }

        zip.finish()
        zip.flush()
    }

    private fun ZipOutputStream.entry(name: String, block: Writer.() -> Unit) {
        putNextEntry(ZipEntry(name))
        val writer = OutputStreamWriter(this, Charsets.UTF_8).buffered()
        writer.block()
        writer.flush()
        closeEntry()
    }


    private fun Writer.writeContentTypes(sheetCount: Int) {
        append(XML_HEADER)
        append("<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">")
        append("<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>")
        append("<Default Extension=\"xml\" ContentType=\"application/xml\"/>")
        append("<Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/>")
        append("<Override PartName=\"/xl/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\"/>")
        for (i in 1..sheetCount) {
            append("<Override PartName=\"/xl/worksheets/sheet$i.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>")
        }
        append("</Types>")
    }

    private fun Writer.writeRootRels() {
        append(XML_HEADER)
        append("<Relationships xmlns=\"$REL_NS\">")
        append("<Relationship Id=\"rId1\" Type=\"$DOC_REL_NS/officeDocument\" Target=\"xl/workbook.xml\"/>")
        append("</Relationships>")
    }

    private fun Writer.writeWorkbook(sheetNames: List<String>) {
        append(XML_HEADER)
        append("<workbook xmlns=\"$MAIN_NS\" xmlns:r=\"$DOC_REL_NS\"><sheets>")
        sheetNames.forEachIndexed { index, name ->
            append("<sheet name=\"${escape(name)}\" sheetId=\"${index + 1}\" r:id=\"rId${index + 1}\"/>")
        }
        append("</sheets></workbook>")
    }

    private fun Writer.writeWorkbookRels(sheetCount: Int) {
        append(XML_HEADER)
        append("<Relationships xmlns=\"$REL_NS\">")
        for (i in 1..sheetCount) {
            append("<Relationship Id=\"rId$i\" Type=\"$DOC_REL_NS/worksheet\" Target=\"worksheets/sheet$i.xml\"/>")
        }
        append("<Relationship Id=\"rId${sheetCount + 1}\" Type=\"$DOC_REL_NS/styles\" Target=\"styles.xml\"/>")
        append("</Relationships>")
    }

    private fun Writer.writeStyles() {
        append(XML_HEADER)
        append("<styleSheet xmlns=\"$MAIN_NS\">")
        append("<fonts count=\"2\"><font><sz val=\"11\"/><name val=\"Calibri\"/></font><font><b/><sz val=\"11\"/><name val=\"Calibri\"/></font></fonts>")
        append("<fills count=\"3\"><fill><patternFill patternType=\"none\"/></fill><fill><patternFill patternType=\"gray125\"/></fill>")
        append("<fill><patternFill patternType=\"solid\"><fgColor rgb=\"FFE3F4E3\"/><bgColor indexed=\"64\"/></patternFill></fill></fills>")
        append("<borders count=\"1\"><border><left/><right/><top/><bottom/><diagonal/></border></borders>")
        append("<cellStyleXfs count=\"1\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\"/></cellStyleXfs>")
        append("<cellXfs count=\"2\">")
        append("<xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\" xfId=\"0\"/>")
        append("<xf numFmtId=\"0\" fontId=\"1\" fillId=\"2\" borderId=\"0\" xfId=\"0\" applyFont=\"1\" applyFill=\"1\"/>")
        append("</cellXfs>")
        append("<cellStyles count=\"1\"><cellStyle name=\"Normal\" xfId=\"0\" builtinId=\"0\"/></cellStyles>")
        append("</styleSheet>")
    }


    private fun Writer.writeSheet(sheet: Sheet) {
        append(XML_HEADER)
        append("<worksheet xmlns=\"$MAIN_NS\">")

        append("<sheetViews><sheetView workbookViewId=\"0\"")
        if (sheet.rightToLeft) append(" rightToLeft=\"1\"")
        append(">")
        if (sheet.headers.isNotEmpty()) {
            append("<pane ySplit=\"1\" topLeftCell=\"A2\" activePane=\"bottomLeft\" state=\"frozen\"/>")
            append("<selection pane=\"bottomLeft\"/>")
        }
        append("</sheetView></sheetViews>")

        val widths = columnWidths(sheet)
        if (widths.isNotEmpty()) {
            append("<cols>")
            widths.forEachIndexed { index, width ->
                append("<col min=\"${index + 1}\" max=\"${index + 1}\" width=\"$width\" customWidth=\"1\"/>")
            }
            append("</cols>")
        }

        append("<sheetData>")
        var rowNumber = 1
        if (sheet.headers.isNotEmpty()) {
            writeRow(rowNumber++, sheet.headers.map { Cell.Text(it) }, HEADER_STYLE)
        }
        sheet.rows.forEach { row -> writeRow(rowNumber++, row, DEFAULT_STYLE) }
        append("</sheetData></worksheet>")
    }

    private fun Writer.writeRow(rowNumber: Int, cells: List<Cell>, style: Int) {
        val styleAttr = if (style != DEFAULT_STYLE) " s=\"$style\"" else ""
        append("<row r=\"$rowNumber\">")
        cells.forEachIndexed { index, cell ->
            val ref = columnName(index) + rowNumber
            when (cell) {
                is Cell.Text -> {
                    append("<c r=\"$ref\" t=\"inlineStr\"$styleAttr><is><t xml:space=\"preserve\">")
                    append(escape(cell.value))
                    append("</t></is></c>")
                }
                is Cell.Number -> append("<c r=\"$ref\"$styleAttr><v>${formatNumber(cell.value)}</v></c>")
                Cell.Empty -> if (style != DEFAULT_STYLE) append("<c r=\"$ref\"$styleAttr/>")
            }
        }
        append("</row>")
    }

    private fun columnWidths(sheet: Sheet): List<Int> {
        val columnCount = maxOf(sheet.headers.size, sheet.rows.maxOfOrNull { it.size } ?: 0)
        if (columnCount == 0) return emptyList()

        val lengths = IntArray(columnCount)
        sheet.headers.forEachIndexed { i, h -> lengths[i] = maxOf(lengths[i], h.length) }
        sheet.rows.forEach { row ->
            row.forEachIndexed { i, cell ->
                val length = when (cell) {
                    is Cell.Text -> cell.value.length
                    is Cell.Number -> formatNumber(cell.value).length
                    Cell.Empty -> 0
                }
                if (length > lengths[i]) lengths[i] = length
            }
        }
        return lengths.map { (it + 3).coerceIn(MIN_COLUMN_WIDTH, MAX_COLUMN_WIDTH) }
    }


    private fun uniqueSheetNames(sheets: List<Sheet>): List<String> {
        val used = mutableSetOf<String>()
        return sheets.mapIndexed { index, sheet ->
            var name = sheet.name
                .replace(INVALID_SHEET_CHARS, " ")
                .trim()
                .take(MAX_SHEET_NAME_LENGTH)
                .ifEmpty { "Sheet${index + 1}" }
            if (name.lowercase() in used) name = "${name.take(MAX_SHEET_NAME_LENGTH - 4)} ${index + 1}"
            used += name.lowercase()
            name
        }
    }

    /** A, B, ..., Z, AA, AB, ... */
    private fun columnName(index: Int): String {
        var n = index
        val builder = StringBuilder()
        do {
            builder.insert(0, ('A' + n % 26))
            n = n / 26 - 1
        } while (n >= 0)
        return builder.toString()
    }

    private fun formatNumber(value: Double): String = when {
        value.isNaN() || value.isInfinite() -> "0"
        value == Math.rint(value) && abs(value) < 1e15 -> value.toLong().toString()
        else -> BigDecimal(value.toString()).toPlainString()
    }

    private fun escape(value: String): String = buildString(value.length) {
        for (ch in value) {
            when {
                ch == '&' -> append("&amp;")
                ch == '<' -> append("&lt;")
                ch == '>' -> append("&gt;")
                ch == '"' -> append("&quot;")
                isInvalidXmlChar(ch) -> Unit
                else -> append(ch)
            }
        }
    }

    private fun isInvalidXmlChar(ch: Char): Boolean =
        (ch < ' ' && ch != '\t' && ch != '\n' && ch != '\r') || ch == '\uFFFE' || ch == '\uFFFF'

    private companion object {
        const val XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        const val MAIN_NS = "http://schemas.openxmlformats.org/spreadsheetml/2006/main"
        const val REL_NS = "http://schemas.openxmlformats.org/package/2006/relationships"
        const val DOC_REL_NS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships"

        const val DEFAULT_STYLE = 0
        const val HEADER_STYLE = 1
        const val MIN_COLUMN_WIDTH = 10
        const val MAX_COLUMN_WIDTH = 50
        const val MAX_SHEET_NAME_LENGTH = 31
        val INVALID_SHEET_CHARS = Regex("[\\\\/?*\\[\\]:]")
    }
}