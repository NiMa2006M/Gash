package com.example.gash.core.export

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream

class XlsxSpreadsheetWriterTest {

    private val writer = XlsxSpreadsheetWriter()

    private fun write(vararg sheets: Sheet): Map<String, String> {
        val output = ByteArrayOutputStream()
        writer.write(Spreadsheet(sheets.toList()), output)

        val entries = mutableMapOf<String, String>()
        ZipInputStream(ByteArrayInputStream(output.toByteArray())).use { zip ->
            var entry = zip.nextEntry
            while (entry != null) {
                entries[entry.name] = zip.readBytes().toString(Charsets.UTF_8)
                entry = zip.nextEntry
            }
        }
        return entries
    }

    private fun sheet(name: String = "S", rows: List<List<Cell>> = emptyList(), headers: List<String> = listOf("H")) =
        Sheet(name = name, headers = headers, rows = rows)

    @Test
    fun `package contains all required parts`() {
        val entries = write(sheet())

        listOf(
            "[Content_Types].xml", "_rels/.rels", "xl/workbook.xml",
            "xl/_rels/workbook.xml.rels", "xl/styles.xml", "xl/worksheets/sheet1.xml"
        ).forEach { assertTrue("missing $it", it in entries) }
    }

    @Test
    fun `text is xml escaped and invalid control chars are dropped`() {
        val entries = write(
            sheet(
                headers = listOf("A & B"),
                rows = listOf(listOf(Cell.Text("<x>\u0001ok")))
            )
        )
        val xml = entries.getValue("xl/worksheets/sheet1.xml")

        assertTrue(xml.contains("A &amp; B"))
        assertTrue(xml.contains("&lt;x&gt;ok"))
        assertFalse(xml.contains("\u0001"))
    }

    @Test
    fun `numbers are written without trailing zero`() {
        val entries = write(sheet(rows = listOf(listOf(Cell.Number(12.0), Cell.Number(2.5)))))
        val xml = entries.getValue("xl/worksheets/sheet1.xml")

        assertTrue(xml.contains("<v>12</v>"))
        assertTrue(xml.contains("<v>2.5</v>"))
    }

    @Test
    fun `empty cells are not written`() {
        val entries = write(sheet(rows = listOf(listOf(Cell.Empty, Cell.Text("x")))))
        val xml = entries.getValue("xl/worksheets/sheet1.xml")

        assertFalse(xml.contains("r=\"A2\""))
        assertTrue(xml.contains("r=\"B2\""))
    }

    @Test
    fun `sheet names are sanitized and kept within excel limits`() {
        val entries = write(sheet(name = "a/b:c*?[]" + "x".repeat(40)))
        val workbook = entries.getValue("xl/workbook.xml")
        val name = Regex("name=\"([^\"]*)\"").find(workbook)!!.groupValues[1]

        assertTrue(name.length <= 31)
        assertFalse(name.any { it in "/\\?*[]:" })
    }

    @Test
    fun `duplicate sheet names are made unique and each sheet gets its own part`() {
        val entries = write(sheet(name = "Same"), sheet(name = "same"))

        assertTrue("xl/worksheets/sheet2.xml" in entries)
        val names = Regex("<sheet name=\"([^\"]*)\"").findAll(entries.getValue("xl/workbook.xml"))
            .map { it.groupValues[1].lowercase() }.toList()
        assertEquals(names.size, names.toSet().size)
    }

    @Test
    fun `right to left sheets are flagged`() {
        val entries = write(sheet().copy(rightToLeft = true))

        assertTrue(entries.getValue("xl/worksheets/sheet1.xml").contains("rightToLeft=\"1\""))
    }
}