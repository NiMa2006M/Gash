package com.example.gash.feature.herd

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gash.R
import com.example.gash.ui.helper.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterNewAnimalSheet(
    error: UiText?,
    isSubmitting: Boolean,
    onConfirm: (rfidCode: String?) -> Unit,
    onDismiss: () -> Unit
) {
    var rfidCode by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
            Text(text = stringResource(R.string.herd_detail_register_new_title))

            OutlinedTextField(
                value = rfidCode,
                onValueChange = { rfidCode = it },
                placeholder = { Text(stringResource(R.string.herd_detail_rfid_optional_hint)) },
                isError = error != null,
                supportingText = error?.let { { Text(it.asString()) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
            )

            Button(
                onClick = { onConfirm(rfidCode) },
                enabled = !isSubmitting,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text(text = stringResource(R.string.common_btn_add))
            }
        }
    }
}