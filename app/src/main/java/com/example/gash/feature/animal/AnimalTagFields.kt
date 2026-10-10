package com.example.gash.feature.animal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gash.R
import com.example.gash.domain.model.AnimalTags
import com.example.gash.domain.model.TagConfig

@Composable
fun AnimalTagFields(
    tagConfigs: List<TagConfig>,
    values: AnimalTags,
    onValueChange: (slot: Int, value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        tagConfigs.forEach { config ->
            val label = config.name.ifBlank {
                stringResource(R.string.settings_tag_default_name, config.slot)
            }
            OutlinedTextField(
                value = values.valueFor(config.slot).orEmpty(),
                onValueChange = { onValueChange(config.slot, it.take(AnimalTags.MAX_VALUE_LENGTH)) },
                label = { Text(label) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}