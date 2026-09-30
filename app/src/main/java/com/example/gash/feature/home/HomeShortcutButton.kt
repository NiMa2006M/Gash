package com.example.gash.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.ui.theme.GashGreen

@Composable
fun HomeShortcutButton(
    page: HomePage,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .selectable(
                selected = selected,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Tab,
                onClick = onClick
            )
            .padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = if (selected) Color.White else Color.White.copy(alpha = 0.18f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(page.iconRes),
                contentDescription = null,
                tint = if (selected) GashGreen else Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(page.labelRes),
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}