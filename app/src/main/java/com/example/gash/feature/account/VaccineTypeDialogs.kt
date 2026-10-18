// VaccineTypeDialogs.kt
package com.example.gash.feature.account

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.gash.R
import com.example.gash.ui.helper.UiText

@Composable
fun VaccineTypeFormDialog(
    mode: VaccineTypeDialogMode,
    text: String,
    error: UiText?,
    onTextChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val titleRes = when (mode) {
        is VaccineTypeDialogMode.Add -> R.string.vaccine_type_add_title
        is VaccineTypeDialogMode.Rename -> R.string.vaccine_type_rename_title
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(titleRes)) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = { Text(stringResource(R.string.vaccine_type_name_hint)) },
                isError = error != null,
                supportingText = error?.let { { Text(it.asString()) } },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(stringResource(R.string.common_btn_confirm)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.common_btn_cancel)) }
        }
    )
}

@Composable
fun VaccineTypeArchiveDialog(
    name: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.vaccine_type_archive_title)) },
        text = { Text(stringResource(R.string.vaccine_type_archive_message, name)) },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(stringResource(R.string.common_btn_delete)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.common_btn_cancel)) }
        }
    )
}