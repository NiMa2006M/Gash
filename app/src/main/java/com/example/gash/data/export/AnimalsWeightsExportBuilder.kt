// AnimalsWeightsExportBuilder.kt
package com.example.gash.data.export

import android.content.Context
import com.example.gash.R
import com.example.gash.core.database.dao.ExportDao
import com.example.gash.core.export.Cell
import com.example.gash.core.export.Sheet
import com.example.gash.core.export.Spreadsheet
import com.example.gash.core.time.formatDateTime
import com.example.gash.domain.error.DomainError
import com.example.gash.domain.model.AnimalTags
import com.example.gash.domain.model.ExportRequest
import com.example.gash.domain.repository.TagSettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AnimalsWeightsExportBuilder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val exportDao: ExportDao,
    private val tagSettingsRepository: TagSettingsRepository
) : ExportBuilder {

    override fun canHandle(request: ExportRequest): Boolean = request is ExportRequest.AnimalsWeights

    override suspend fun build(request: ExportRequest): ExportDocument {
        val locale = context.resources.configuration.locales[0]
        val rtl = locale.isRightToLeft()

        val animals = exportDao.getAnimalWeightRows()
        if (animals.isEmpty()) throw DomainError.NothingToExport

        val enabledTags = tagSettingsRepository.settings.first().enabledTags

        val animalHeaders = buildList {
            add(context.getString(R.string.export_col_animal_number))
            add(context.getString(R.string.export_col_rfid))
            add(context.getString(R.string.export_col_herd))
            enabledTags.forEach { tag ->
                add(tag.name.ifBlank { context.getString(R.string.settings_tag_default_name, tag.slot) })
            }
            add(context.getString(R.string.export_col_registered_at))
            add(context.getString(R.string.export_col_last_weight))
            add(context.getString(R.string.export_col_last_weight_date))
        }

        val animalRows = animals.map { row ->
            val tags = AnimalTags(row.tag1, row.tag2, row.tag3)
            buildList<Cell> {
                add(Cell.of(row.animalId))
                add(Cell.of(row.rfidCode))
                add(Cell.of(row.herdName))
                enabledTags.forEach { add(Cell.of(tags.valueFor(it.slot))) }
                add(Cell.of(formatDateTime(row.createdAt, locale)))
                add(Cell.of(row.lastWeightKg))
                add(Cell.of(row.lastWeightAt?.let { formatDateTime(it, locale) }))
            }
        }

        val sheets = mutableListOf(
            Sheet(
                name = context.getString(R.string.export_sheet_animals),
                headers = animalHeaders,
                rows = animalRows,
                rightToLeft = rtl
            )
        )

        val history = exportDao.getWeightHistoryRows()
        if (history.isNotEmpty()) {
            sheets += Sheet(
                name = context.getString(R.string.export_sheet_weight_history),
                headers = listOf(
                    context.getString(R.string.export_col_animal_number),
                    context.getString(R.string.export_col_rfid),
                    context.getString(R.string.export_col_weight),
                    context.getString(R.string.export_col_weight_date)
                ),
                rows = history.map { row ->
                    listOf(
                        Cell.of(row.animalId),
                        Cell.of(row.rfidCode),
                        Cell.of(row.weightKg),
                        Cell.of(formatDateTime(row.recordedAt, locale))
                    )
                },
                rightToLeft = rtl
            )
        }

        return ExportDocument("gash_animals_weights", Spreadsheet(sheets))
    }
}