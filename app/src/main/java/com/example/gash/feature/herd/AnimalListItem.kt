package com.example.gash.feature.herd

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.domain.model.Animal
import com.example.gash.ui.theme.GashFieldBackground
import com.example.gash.ui.theme.GashTextPrimary
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun AnimalListItem(
    animal: Animal,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GashFieldBackground, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(R.string.animal_list_item_title, animal.id),
                color = GashTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            val subtitle = animal.activeRfidCode?.let {
                stringResource(R.string.animal_list_item_rfid, it)
            } ?: stringResource(R.string.animal_list_item_no_rfid)
            Text(text = subtitle, color = GashTextSecondary, fontSize = 12.sp)
        }
    }
}