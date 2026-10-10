package com.example.gash.feature.animal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.domain.model.AnimalTags
import com.example.gash.domain.model.TagConfig
import com.example.gash.ui.helper.UiText
import com.example.gash.ui.theme.GashError_RED

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTagsSheet(
    tagConfigs: List<TagConfig>,
    initialTags: AnimalTags,
    error: UiText?,
    isSubmitting: Boolean,
    onConfirm: (AnimalTags) -> Unit,
    onDismiss: () -> Unit
) {
    var tags by remember { mutableStateOf(initialTags) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.animal_detail_edit_tags_title),
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            AnimalTagFields(
                tagConfigs = tagConfigs,
                values = tags,
                onValueChange = { slot, value -> tags = tags.withValue(slot, value) }
            )

            error?.let {
                Text(
                    text = it.asString(),
                    color = GashError_RED,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = { onConfirm(tags) },
                enabled = !isSubmitting,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.common_btn_save))
            }
        }
    }
}