package com.example.gash.feature.herd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gash.R
import com.example.gash.domain.model.Animal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickExistingAnimalsSheet(
    animals: List<Animal>,
    selectedIds: Set<Long>,
    isSubmitting: Boolean,
    onToggle: (Long) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
            Text(text = stringResource(R.string.herd_detail_pick_existing_title))

            if (animals.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.herd_detail_no_unassigned_animals))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.height(320.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(items = animals, key = { it.id }) { animal ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = animal.id in selectedIds,
                                onCheckedChange = { onToggle(animal.id) }
                            )
                            Text(text = stringResource(R.string.animal_list_item_title, animal.id))
                        }
                    }
                }
            }

            Button(
                onClick = onConfirm,
                enabled = selectedIds.isNotEmpty() && !isSubmitting,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.common_btn_add))
            }
        }
    }
}