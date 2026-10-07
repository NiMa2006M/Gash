package com.example.gash.feature.animal

import com.example.gash.domain.model.Animal
import com.example.gash.domain.model.Herd
import com.example.gash.domain.model.WeightRecord
import com.example.gash.ui.helper.UiText

data class AnimalDetailUiState(
    val animal: Animal? = null,
    val herd: Herd? = null,
    val weightHistory: List<WeightRecord> = emptyList(),
    val isMoveHerdSheetOpen: Boolean = false,
    val availableHerds: List<Herd> = emptyList(),
    val isRecordWeightSheetOpen: Boolean = false,
    val isExpireConfirmOpen: Boolean = false,
    val error: UiText? = null,
    val isSubmitting: Boolean = false
)