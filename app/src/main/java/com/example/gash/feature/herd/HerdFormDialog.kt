package com.example.gash.feature.herd

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.gash.R
import com.example.gash.ui.helper.UiText
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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

        title = {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleLarge
            )
        },

        text = {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,

                modifier = Modifier
                    .fillMaxWidth(),

                singleLine = true,

                shape = RoundedCornerShape(16.dp),

                placeholder = {
                    Text(
                        text = stringResource(R.string.herd_name_hint),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.45f
                        )
                    )
                },

                isError = error != null,

                supportingText = error?.let {
                    {
                        Text(
                            text = it.asString()
                        )
                    }
                },

                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor =
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),

                    focusedContainerColor =
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),

                    unfocusedBorderColor =
                        MaterialTheme.colorScheme.outlineVariant,

                    focusedBorderColor =
                        MaterialTheme.colorScheme.primary,

                    unfocusedPlaceholderColor =
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),

                    focusedPlaceholderColor =
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),

                    errorBorderColor =
                        MaterialTheme.colorScheme.error
                )
            )
        },

        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(R.string.common_btn_confirm))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text(stringResource(R.string.common_btn_cancel)) }
        }
    )
}