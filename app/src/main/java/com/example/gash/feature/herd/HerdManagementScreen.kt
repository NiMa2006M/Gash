package com.example.gash.feature.herd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gash.R
import com.example.gash.core.ui.components.SearchField
import com.example.gash.ui.theme.GashOrange
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun HerdManagementScreen(
    onHerdClick: (Long) -> Unit,
    viewModel: HerdManagementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (uiState.herds.isNotEmpty()) {
                SearchField(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange,
                    placeholder = stringResource(R.string.herd_management_search_hint),
                    clearContentDescription = stringResource(R.string.common_btn_clear),
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(Modifier.weight(1f))
            }

            Button(
                onClick = viewModel::onAddClick,
                modifier = Modifier.height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GashOrange, contentColor = Color.White),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text(text = stringResource(R.string.common_btn_add), fontSize = 13.sp)
            }
        }

        when {
            uiState.herds.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                HerdEmptyState()
            }

            uiState.filteredHerds.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.herd_management_no_search_results),
                    color = GashTextSecondary
                )
            }

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = uiState.filteredHerds, key = { it.id }) { herd ->
                    HerdCard(
                        herd = herd,
                        onClick = { onHerdClick(herd.id) },
                        onRenameClick = { viewModel.onRenameClick(herd) },
                        onDeleteClick = { viewModel.onDeleteClick(herd) }
                    )
                }
            }
        }
    }

    uiState.dialogMode?.let { mode ->
        HerdFormDialog(
            mode = mode,
            text = uiState.dialogText,
            error = uiState.dialogError,
            onTextChange = viewModel::onDialogTextChange,
            onConfirm = viewModel::onDialogConfirm,
            onDismiss = viewModel::onDialogDismiss
        )
    }

    uiState.herdPendingDelete?.let { herd ->
        HerdDeleteConfirmDialog(
            herdName = herd.name,
            onConfirm = viewModel::onDeleteConfirm,
            onDismiss = viewModel::onDeleteDismiss
        )
    }
}