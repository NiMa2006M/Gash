package com.example.gash.feature.herd

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.gash.R
import com.example.gash.ui.helper.UiText

@Composable
fun HerdFormDialog(
    mode: HerdDialogMode,
    text: String,
    error: UiText?,
    onTextChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val titleRes = when (mode) {
        is HerdDialogMode.Add -> R.string.herd_management_add_dialog_title
        is HerdDialogMode.Rename -> R.string.herd_management_rename_dialog_title
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(titleRes)) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = { Text(stringResource(R.string.herd_name_hint)) },
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