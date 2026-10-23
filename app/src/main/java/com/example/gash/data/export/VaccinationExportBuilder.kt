// VaccinationExportBuilder.kt
package com.example.gash.data.export

import android.content.Context
import com.example.gash.R
import com.example.gash.core.database.dao.ExportDao
import com.example.gash.core.export.Cell
import com.example.gash.core.export.Sheet
import com.example.gash.core.export.Spreadsheet
import com.example.gash.core.time.formatDateTime
import com.example.gash.domain.error.DomainError
import com.example.gash.domain.model.ExportRequest
import com.example.gash.domain.model.InjectionSite
import com.example.gash.domain.repository.VaccinationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class VaccinationExportBuilder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val exportDao: ExportDao,
    private val vaccinationRepository: VaccinationRepository
) : ExportBuilder {

    override fun canHandle(request: ExportRequest): Boolean =
        request is ExportRequest.VaccinationOperation

    override suspend fun build(request: ExportRequest): ExportDocument {
        val operationRequest = request as ExportRequest.VaccinationOperation
        val locale = context.resources.configuration.locales[0]

        val operation = vaccinationRepository.getOperation(operationRequest.operationId)
            ?: throw DomainError.ExportSourceNotFound
        val records = exportDao.getVaccinationRows(operation.id)
        if (records.isEmpty()) throw DomainError.NothingToExport

        val headers = listOf(
            context.getString(R.string.export_col_operation_number),
            context.getString(R.string.export_col_vaccine),
            context.getString(R.string.export_col_site),
            context.getString(R.string.export_col_dose),
            context.getString(R.string.export_col_operator),
            context.getString(R.string.export_col_operation_herd),
            context.getString(R.string.export_col_record_time),
            context.getString(R.string.export_col_animal_number),
            context.getString(R.string.export_col_rfid),
            context.getString(R.string.export_col_animal_current_herd)
        )

        val siteLabel = context.getString(operation.injectionSite.labelRes())

        val rows = records.map { record ->
            listOf(
                Cell.of(operation.id),
                Cell.of(operation.vaccineTypeName),
                Cell.of(siteLabel),
                Cell.of(operation.doseMl),
                Cell.of(operation.operatorName),
                Cell.of(operation.herdName),
                Cell.of(formatDateTime(record.recordedAt, locale)),
                Cell.of(record.animalId),
                Cell.of(record.scannedRfidCode),
                Cell.of(record.animalHerdName)
            )
        }

        val sheet = Sheet(
            name = context.getString(R.string.export_sheet_vaccination, operation.id),
            headers = headers,
            rows = rows,
            rightToLeft = locale.isRightToLeft()
        )
        return ExportDocument("gash_vaccination_op${operation.id}", Spreadsheet(listOf(sheet)))
    }

    private fun InjectionSite.labelRes(): Int = when (this) {
        InjectionSite.INTRAMUSCULAR -> R.string.vaccination_site_intramuscular
        InjectionSite.SUBCUTANEOUS -> R.string.vaccination_site_subcutaneous
        InjectionSite.INTRAVENOUS -> R.string.vaccination_site_intravenous
    }
}