package com.example.gash.domain.usecase.herd

import com.example.gash.domain.model.Herd
import com.example.gash.domain.repository.HerdRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveHerdUseCase @Inject constructor(
    private val repository: HerdRepository
) {
    operator fun invoke(herdId: Long): Flow<Herd?> = repository.observeHerd(herdId)
}