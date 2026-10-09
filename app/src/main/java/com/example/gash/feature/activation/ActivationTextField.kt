package com.example.gash.feature.activation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gash.ui.theme.GashError_RED
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun ActivationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    isError: Boolean = false,
    errorText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = placeholder) },
        isError = isError,
        supportingText = if (isError && errorText != null) {
            { Text(text = errorText) }
        } else null,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = GashTextSecondary,
            errorBorderColor = GashError_RED,
            errorLabelColor = GashError_RED,
            errorSupportingTextColor = GashError_RED
        )
    )
}