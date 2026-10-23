package com.example.gash.feature.filetransfer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.core.time.formatDate
import com.example.gash.domain.model.VaccinationOperationSummary
import com.example.gash.ui.theme.GashBorder
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashOrange
import com.example.gash.ui.theme.GashSurface
import com.example.gash.ui.theme.GashTextPrimary
import com.example.gash.ui.theme.GashTextSecondary
import java.util.Locale

@Composable
fun VaccinationOperationItem(
    summary: VaccinationOperationSummary,
    selected: Boolean,
    locale: Locale,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    val dateText = remember(summary.startedAt, locale) { formatDate(summary.startedAt, locale) }
    val countText = stringResource(R.string.file_transfer_operation_animals_count, summary.recordCount)
    val details = listOfNotNull(summary.vaccineTypeName, countText, summary.herdName).joinToString(" · ")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (selected) GashGreen.copy(alpha = 0.06f) else GashSurface)
            .border(width = if (selected) 2.dp else 1.dp, color = if (selected) GashGreen else GashBorder, shape = shape)
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick)
            .padding(start = 4.dp, end = 14.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.padding(start = 8.dp),
            colors = RadioButtonDefaults.colors(selectedColor = GashGreen)
        )

        Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.file_transfer_operation_title, summary.id),
                    color = GashTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                if (summary.isOpen) {
                    Text(
                        text = stringResource(R.string.file_transfer_operation_in_progress),
                        color = GashOrange,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .background(GashOrange.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            Text(
                text = details,
                color = GashTextSecondary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(text = dateText, color = GashTextSecondary, fontSize = 12.sp)
    }
}