package com.example.gash.feature.vaccination

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.domain.model.InjectionSite
import com.example.gash.ui.theme.GashBorder
import com.example.gash.ui.theme.GashError_RED
import com.example.gash.ui.theme.GashFieldBackground
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashOptionBorder
import com.example.gash.ui.theme.GashSurface
import com.example.gash.ui.theme.GashTextPrimary
import com.example.gash.ui.theme.GashTextSecondary

private val FieldShape = RoundedCornerShape(14.dp)

@Composable
fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = GashTextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(GashSurface)
                .border(1.dp, GashBorder, shape)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content
        )
    }
}

@Composable
private fun SettingsFieldLabel(label: String, required: Boolean) {
    Row {
        Text(text = label, color = GashTextSecondary, fontSize = 13.sp)
        if (required) {
            Text(text = " *", color = GashError_RED, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SettingsPickerField(
    label: String,
    required: Boolean,
    value: String?,
    placeholder: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SettingsFieldLabel(label, required)
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp)
                .clip(FieldShape)
                .background(GashFieldBackground)
                .clickable(enabled = enabled, onClick = onClick)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value ?: placeholder,
                color = when {
                    value == null -> GashTextSecondary
                    enabled -> GashTextPrimary
                    else -> GashTextPrimary.copy(alpha = 0.75f)
                },
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if (enabled) {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = GashTextSecondary
                )
            }
        }
    }
}

@Composable
fun SettingsTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    suffix: String? = null,
    errorText: String? = null
) {
    val isError = errorText != null

    Column(modifier = modifier.fillMaxWidth()) {
        SettingsFieldLabel(label, required = false)
        Spacer(Modifier.height(6.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = true,
            textStyle = TextStyle(
                color = if (enabled) GashTextPrimary else GashTextPrimary.copy(alpha = 0.75f),
                fontSize = 15.sp
            ),
            cursorBrush = SolidColor(GashGreen),
            keyboardOptions = keyboardOptions,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp)
                        .clip(FieldShape)
                        .background(GashFieldBackground)
                        .then(if (isError) Modifier.border(1.dp, GashError_RED, FieldShape) else Modifier)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(text = placeholder, color = GashTextSecondary, fontSize = 15.sp)
                        }
                        innerTextField()
                    }
                    if (suffix != null) {
                        Spacer(Modifier.padding(start = 8.dp))
                        Text(text = suffix, color = GashTextSecondary, fontSize = 14.sp)
                    }
                }
            }
        )
        if (errorText != null) {
            Text(
                text = errorText,
                color = GashError_RED,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}
@Composable
fun InjectionSiteSelector(
    selected: InjectionSite?,
    enabled: Boolean,
    onSelected: (InjectionSite) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SettingsFieldLabel(stringResource(R.string.vaccination_field_site), required = true)
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InjectionSite.entries.forEach { site ->
                val isSelected = site == selected
                val optionShape = RoundedCornerShape(12.dp)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                        .clip(optionShape)
                        .background(if (isSelected) GashGreen else GashFieldBackground)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) GashGreen else GashOptionBorder,
                            shape = optionShape
                        )
                        .selectable(
                            selected = isSelected,
                            enabled = enabled,
                            role = Role.RadioButton,
                            onClick = { onSelected(site) }
                        )
                        .padding(horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(site.labelRes()),
                        color = when {
                            isSelected -> Color.White
                            enabled -> GashTextPrimary
                            else -> GashTextPrimary.copy(alpha = 0.5f)
                        },
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun OperatorSuggestions(
    names: List<String>,
    current: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.vaccination_operator_recent),
            color = GashTextSecondary,
            fontSize = 12.sp
        )
        names.forEach { name ->
            val isSelected = name == current.trim()
            Text(
                text = name,
                color = if (isSelected) Color.White else GashGreen,
                fontSize = 12.sp,
                maxLines = 1,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) GashGreen else GashGreen.copy(alpha = 0.1f))
                    .clickable { onSelect(name) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}