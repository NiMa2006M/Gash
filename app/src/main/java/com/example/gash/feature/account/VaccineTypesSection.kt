package com.example.gash.feature.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gash.R
import com.example.gash.domain.model.VaccineType
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun VaccineTypesSection(
    viewModel: VaccineTypesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SectionTitle(title = stringResource(R.string.settings_vaccines_section))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            if (uiState.types.isEmpty()) {
                Text(
                    text = stringResource(R.string.settings_vaccines_empty),
                    color = GashTextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            } else {
                uiState.types.forEachIndexed { index, type ->
                    VaccineTypeRow(
                        type = type,
                        onRenameClick = { viewModel.onRenameClick(type) },
                        onDeleteClick = { viewModel.onArchiveClick(type) }
                    )
                    if (index < uiState.types.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )
                    }
                }
            }

            TextButton(
                onClick = viewModel::onAddClick,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = GashGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(text = stringResource(R.string.settings_vaccines_add), color = GashGreen)
            }
        }
    }

    uiState.dialogMode?.let { mode ->
        VaccineTypeFormDialog(
            mode = mode,
            text = uiState.dialogText,
            error = uiState.dialogError,
            onTextChange = viewModel::onDialogTextChange,
            onConfirm = viewModel::onDialogConfirm,
            onDismiss = viewModel::onDialogDismiss
        )
    }

    uiState.typePendingArchive?.let { type ->
        VaccineTypeArchiveDialog(
            name = type.name,
            onConfirm = viewModel::onArchiveConfirm,
            onDismiss = viewModel::onArchiveDismiss
        )
    }
}

@Composable
private fun VaccineTypeRow(
    type: VaccineType,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = type.name,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Box {
            IconButton(onClick = { isMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.common_btn_more),
                    tint = GashTextSecondary
                )
            }
            DropdownMenu(expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.herd_menu_rename)) },
                    onClick = { isMenuExpanded = false; onRenameClick() }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.herd_menu_delete)) },
                    onClick = { isMenuExpanded = false; onDeleteClick() }
                )
            }
        }
    }
}