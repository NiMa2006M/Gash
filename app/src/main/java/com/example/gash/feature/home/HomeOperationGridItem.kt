package com.example.gash.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.ui.theme.GashGreen

@Composable
fun HomeOperationGridItem(
    operation: HomeOperation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(role = Role.Button, onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(GashGreen.copy(alpha = 0.12f), RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            operation.iconRes?.let { iconRes ->
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(0.6f)
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(operation.labelRes),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}