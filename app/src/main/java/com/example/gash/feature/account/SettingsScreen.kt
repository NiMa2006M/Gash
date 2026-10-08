package com.example.gash.feature.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gash.R
import com.example.gash.core.locale.AppLanguage
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val tagSettings by viewModel.tagSettings.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_btn_back))
            }
            Text(text = stringResource(R.string.account_settings), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(text = stringResource(R.string.settings_language_section), color = GashTextSecondary, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            AppLanguage.entries.forEach { language ->
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { viewModel.onLanguageSelected(language) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = language == currentLanguage, onClick = { viewModel.onLanguageSelected(language) })
                    Text(text = stringResource(language.labelRes))
                }
            }

            Spacer(Modifier.height(20.dp))
            Text(text = stringResource(R.string.settings_tags_section), color = GashTextSecondary, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))

            TagToggleRow(label = stringResource(R.string.settings_tag1), checked = tagSettings.isTag1Enabled) {
                viewModel.onTagToggle(1, it)
            }
            TagToggleRow(label = stringResource(R.string.settings_tag2), checked = tagSettings.isTag2Enabled) {
                viewModel.onTagToggle(2, it)
            }
            TagToggleRow(label = stringResource(R.string.settings_tag3), checked = tagSettings.isTag3Enabled) {
                viewModel.onTagToggle(3, it)
            }
        }
    }
}

@Composable
private fun TagToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}