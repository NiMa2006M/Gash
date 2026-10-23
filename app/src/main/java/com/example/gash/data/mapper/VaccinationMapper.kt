package com.example.gash.data.mapper

import com.example.gash.core.database.dao.VaccinationOperationRow
import com.example.gash.core.database.dao.VaccinationOperationSummaryRow
import com.example.gash.core.database.entity.VaccinationRecordEntity
import com.example.gash.core.database.entity.VaccineTypeEntity
import com.example.gash.domain.model.InjectionSite
import com.example.gash.domain.model.VaccinationOperation
import com.example.gash.domain.model.VaccinationOperationSummary
import com.example.gash.domain.model.VaccinationRecord
import com.example.gash.domain.model.VaccineType

fun VaccineTypeEntity.toDomain(): VaccineType =
    VaccineType(id = id, name = name, isArchived = isArchived)

fun VaccinationOperationRow.toDomain(): VaccinationOperation = VaccinationOperation(
    id = operation.id,
    vaccineTypeId = operation.vaccineTypeId,
    vaccineTypeName = vaccineTypeName,
    injectionSite = InjectionSite.valueOf(operation.injectionSite),
    doseMl = operation.doseMl,
    herdId = operation.herdId,
    herdName = operation.herdNameSnapshot,
    startedAt = operation.startedAt,
    finishedAt = operation.finishedAt,
    operatorName = operation.operatorName
)

fun VaccinationRecordEntity.toDomain(): VaccinationRecord = VaccinationRecord(
    id = id,
    operationId = operationId,
    animalId = animalId,
    scannedRfidCode = scannedRfidCode,
    recordedAt = recordedAt
)

fun VaccinationOperationSummaryRow.toDomain(): VaccinationOperationSummary = VaccinationOperationSummary(
    id = id,
    vaccineTypeName = vaccineTypeName,
    startedAt = startedAt,
    finishedAt = finishedAt,
    herdName = herdName,
    operatorName = operatorName,
    recordCount = recordCount
)