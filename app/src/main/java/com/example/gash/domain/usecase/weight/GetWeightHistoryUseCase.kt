package com.example.gash.domain.usecase.weight

import com.example.gash.domain.model.WeightRecord
import com.example.gash.domain.repository.WeightRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeightHistoryUseCase @Inject constructor(
    private val repository: WeightRecordRepository
) {
    operator fun invoke(animalId: Long): Flow<List<WeightRecord>> = repository.observeHistory(animalId)
}