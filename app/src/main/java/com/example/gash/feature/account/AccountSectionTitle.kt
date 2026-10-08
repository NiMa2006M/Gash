package com.example.gash.feature.account

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.ui.theme.GashGreen

@Composable
fun AccountSectionTitle(
    title: String
) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 20.dp),
        color = GashGreen,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    )
}