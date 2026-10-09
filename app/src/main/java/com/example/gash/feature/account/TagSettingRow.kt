package com.example.gash.feature.account

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.domain.model.TagConfig
import com.example.gash.domain.model.TagSettings
import com.example.gash.feature.activation.ActivationTextField
import com.example.gash.ui.theme.GashGreen

@Composable
fun TagSettingRow(
    config: TagConfig,
    onEnabledChange: (Boolean) -> Unit,
    onNameChange: (String) -> Unit
) {
    var nameText by remember(config.slot) { mutableStateOf(config.name) }
    val defaultName = stringResource(R.string.settings_tag_default_name, config.slot)

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = config.name.ifBlank { defaultName },
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = config.isEnabled,
                onCheckedChange = onEnabledChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.surface,
                    checkedTrackColor = GashGreen
                )
            )
        }

        AnimatedVisibility(visible = config.isEnabled) {
            Column {
                Spacer(Modifier.height(8.dp))
                ActivationTextField(
                    value = nameText,
                    onValueChange = { input ->
                        val limited = input.take(TagSettings.MAX_NAME_LENGTH)
                        nameText = limited
                        onNameChange(limited)
                    },
                    placeholder = stringResource(R.string.settings_tag_name_hint),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}