package com.example.gash.feature.herd

import com.example.gash.domain.model.Animal
import com.example.gash.domain.model.Herd
import com.example.gash.ui.helper.UiText

data class HerdDetailUiState(
    val herd: Herd? = null,
    val animals: List<Animal> = emptyList(),
    val searchQuery: String = "",
    val filteredAnimals: List<Animal> = emptyList(),
    val addSheetMode: AddAnimalMode? = null,
    val unassignedAnimals: List<Animal> = emptyList(),
    val selectedUnassignedIds: Set<Long> = emptySet(),
    val error: UiText? = null,
    val isSubmitting: Boolean = false
)

sealed interface AddAnimalMode {
    data object ChooseMethod : AddAnimalMode
    data object PickExisting : AddAnimalMode
    data object RegisterNew : AddAnimalMode
}