package com.example.gash.feature.herd

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.gash.R

@Composable
fun HerdDeleteConfirmDialog(
    herdName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.herd_management_delete_dialog_title)) },
        text = { Text(stringResource(R.string.herd_management_delete_dialog_message, herdName)) },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(stringResource(R.string.common_btn_delete)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.common_btn_cancel)) }
        }
    )
}