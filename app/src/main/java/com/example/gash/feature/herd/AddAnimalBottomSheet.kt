package com.example.gash.feature.herd

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gash.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAnimalBottomSheet(
    onPickExisting: () -> Unit,
    onRegisterNew: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Text(
                text = stringResource(R.string.herd_detail_add_sheet_title),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            ListItem(
                headlineContent = { Text(stringResource(R.string.herd_detail_add_existing)) },
                supportingContent = { Text(stringResource(R.string.herd_detail_add_existing_desc)) },
                modifier = Modifier.clickable(onClick = onPickExisting)
            )
            ListItem(
                headlineContent = { Text(stringResource(R.string.herd_detail_add_new)) },
                supportingContent = { Text(stringResource(R.string.herd_detail_add_new_desc)) },
                modifier = Modifier.clickable(onClick = onRegisterNew)
            )
            ListItem(
                headlineContent = { Text(stringResource(R.string.herd_detail_add_excel)) },
                supportingContent = { Text(stringResource(R.string.herd_detail_add_excel_soon)) }
            )
        }
    }
}