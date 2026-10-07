package com.example.gash.feature.animal

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
import com.example.gash.domain.model.Herd

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoveHerdSheet(
    herds: List<Herd>,
    currentHerdId: Long?,
    onHerdSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Text(
                text = stringResource(R.string.animal_detail_move_herd_title),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            herds.forEach { herd ->
                ListItem(
                    headlineContent = { Text(herd.name) },
                    supportingContent = {
                        if (herd.id == currentHerdId) {
                            Text(stringResource(R.string.animal_detail_current_herd))
                        }
                    },
                    modifier = Modifier.clickable(enabled = herd.id != currentHerdId) {
                        onHerdSelected(herd.id)
                    }
                )
            }
        }
    }
}