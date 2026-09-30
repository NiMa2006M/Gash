package com.example.gash.domain.usecase.herd

import com.example.gash.domain.repository.HerdRepository
import javax.inject.Inject

class DeleteHerdUseCase @Inject constructor(
    private val repository: HerdRepository
) {
    suspend operator fun invoke(herdId: Long) {
        repository.deleteHerd(herdId)
    }
}