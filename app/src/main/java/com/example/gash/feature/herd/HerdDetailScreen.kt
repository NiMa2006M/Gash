package com.example.gash.feature.herd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun HerdDetailScreen(
    uiState: HerdDetailUiState,
    onBack: () -> Unit,
    onAnimalClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onDismissAddSheet: () -> Unit,
    onPickExistingSelected: () -> Unit,
    onRegisterNewSelected: () -> Unit,
    onToggleUnassignedSelection: (Long) -> Unit,
    onConfirmAddExisting: () -> Unit,
    onConfirmRegisterNew: (String?) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.herd_detail_add_sheet_title))
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_btn_back))
                }
                Column {
                    Text(
                        text = uiState.herd?.name.orEmpty(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.herd_management_animal_count, uiState.herd?.animalCount ?: 0),
                        color = GashTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            if (uiState.animals.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.herd_detail_empty_animals), color = GashTextSecondary)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
                ) {
                    this.items(
                        items = uiState.animals,
                        key = { it.id }
                    ) { animal ->
                        AnimalCard(
                            animal = animal,
                            onClick = {
                                onAnimalClick(animal.id)
                            }
                        )
                    }
                }
            }
        }
    }

    when (uiState.addSheetMode) {
        AddAnimalMode.ChooseMethod -> AddAnimalBottomSheet(
            onPickExisting = onPickExistingSelected,
            onRegisterNew = onRegisterNewSelected,
            onDismiss = onDismissAddSheet
        )
        AddAnimalMode.PickExisting -> PickExistingAnimalsSheet(
            animals = uiState.unassignedAnimals,
            selectedIds = uiState.selectedUnassignedIds,
            isSubmitting = uiState.isSubmitting,
            onToggle = onToggleUnassignedSelection,
            onConfirm = onConfirmAddExisting,
            onDismiss = onDismissAddSheet
        )
        AddAnimalMode.RegisterNew -> RegisterNewAnimalSheet(
            error = uiState.error,
            isSubmitting = uiState.isSubmitting,
            onConfirm = onConfirmRegisterNew,
            onDismiss = onDismissAddSheet
        )
        null -> Unit
    }
}