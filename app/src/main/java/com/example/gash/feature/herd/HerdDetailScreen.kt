package com.example.gash.feature.herd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashSurface
import com.example.gash.ui.theme.GashTextPrimary
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun HerdDetailScreen(
    uiState: HerdDetailUiState,
    onBack: () -> Unit,
    onAnimalClick: (Long) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onDismissAddSheet: () -> Unit,
    onPickExistingSelected: () -> Unit,
    onRegisterNewSelected: () -> Unit,
    onToggleUnassignedSelection: (Long) -> Unit,
    onConfirmAddExisting: () -> Unit,
    onConfirmRegisterNew: (String?) -> Unit
) {
    Scaffold(
        containerColor = GashSurface,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                modifier = Modifier.navigationBarsPadding(),
                containerColor = GashGreen,
                contentColor = GashSurface,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(
                        R.string.herd_detail_add_sheet_title
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        end = 20.dp,
                        top = 12.dp,
                        bottom = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(
                            R.string.common_btn_back
                        ),
                        tint = GashTextPrimary
                    )
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = uiState.herd?.name.orEmpty(),
                        color = GashTextPrimary,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = stringResource(
                            R.string.herd_management_animal_count,
                            uiState.herd?.animalCount ?: 0
                        ),
                        color = GashTextSecondary,
                        fontSize = 13.sp
                    )
                }

//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(14.dp))
//                        .background(
//                            GashGreen.copy(alpha = 0.10f)
//                        )
//                        .padding(
//                            horizontal = 12.dp,
//                            vertical = 9.dp
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Check,
//                        contentDescription = null,
//                        tint = GashGreen
//                    )
//                }
            }

            if (uiState.animals.isNotEmpty()) {
                HerdSearchField(query = uiState.searchQuery, onQueryChange = onSearchQueryChange)
                Spacer(Modifier.height(8.dp))
            }

            when {
                uiState.animals.isEmpty() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.icon_animal_add),
                                contentDescription = null,
                                tint = GashGreen,
                                modifier = Modifier.size(64.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = stringResource(
                                    R.string.herd_detail_empty_animals
                                ),
                                color = GashTextSecondary,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
                uiState.filteredAnimals.isEmpty() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.icon_search_off),
                                contentDescription = null,
                                tint = GashGreen,
                                modifier = Modifier.size(64.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = stringResource(
                                    R.string.herd_detail_no_search_results
                                ),
                                color = GashTextSecondary,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    this.items(items = uiState.filteredAnimals, key = { it.id }) { animal ->
                        AnimalCard(animal = animal, onClick = { onAnimalClick(animal.id) })
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