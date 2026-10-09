package com.example.gash.feature.account

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.core.ui.components.AvatarPlaceholder
import com.example.gash.core.ui.components.ProfileAvatar
import com.example.gash.ui.theme.GashGreen

@Composable
fun AccountHeader(
    farmName: String,
    profileImagePath: String?,
    onAvatarClick: () -> Unit,
    isMenuExpanded: Boolean,
    onMenuClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(GashGreen)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .statusBarsPadding()
            .padding(
                start = 16.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 28.dp
            )
    ) {

        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.common_btn_more),
                    tint = Color.White
                )
            }

            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = onDismissMenu
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.account_logout)) },
                    onClick = onLogout
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 48.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.White.copy(alpha = 0.2f), shape = CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onAvatarClick),
                contentAlignment = Alignment.Center
            ) {
                ProfileAvatar(imagePath = profileImagePath, size = 72.dp)
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = farmName,
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

        }
    }
}