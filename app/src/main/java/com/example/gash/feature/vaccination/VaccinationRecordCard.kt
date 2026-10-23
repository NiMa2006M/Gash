package com.example.gash.feature.vaccination

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.core.time.formatTime
import com.example.gash.domain.model.VaccinationRecord
import com.example.gash.ui.theme.GashBorder
import com.example.gash.ui.theme.GashError_RED
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashSurface
import com.example.gash.ui.theme.GashTextPrimary
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun VaccinationRecordCard(
    number: Int,
    record: VaccinationRecord,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val locale = LocalConfiguration.current.locales[0]
    val timeText = remember(record.recordedAt, locale) { formatTime(record.recordedAt, locale) }
    val shape = RoundedCornerShape(12.dp)

    Card(
        modifier = Modifier.fillMaxWidth().clip(shape).clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = GashSurface),
        border = BorderStroke(1.dp, GashBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(28.dp).background(GashGreen.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = number.toString(), color = GashGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f).padding(vertical = 10.dp)) {
                Text(
                    text = record.scannedRfidCode
                        ?: stringResource(R.string.vaccination_rfid_unknown),
                    color = GashTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = timeText, color = GashTextSecondary, fontSize = 12.sp)
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.common_btn_delete),
                    tint = GashError_RED
                )
            }
        }
    }
}