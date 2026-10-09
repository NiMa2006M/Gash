package com.example.gash.feature.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gash.R
import com.example.gash.core.ui.components.ProfileAvatar
import com.example.gash.ui.theme.GashError_RED
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashOrange

@Composable
fun ProfilePhotoSection(
    imagePath: String?,
    isProcessing: Boolean,
    onChangeClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(96.dp)) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .clickable(enabled = !isProcessing, onClick = onChangeClick)
            ) {
                ProfileAvatar(
                    imagePath = imagePath,
                    size = 96.dp,
                    placeholderContainerColor = GashGreen.copy(alpha = 0.12f),
                    placeholderContentColor = GashGreen
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(30.dp)
                    .background(GashOrange, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(R.string.general_info_change_photo),
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            if (isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).size(32.dp),
                    strokeWidth = 3.dp
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onChangeClick, enabled = !isProcessing) {
                Text(text = stringResource(R.string.general_info_change_photo))
            }
            if (imagePath != null) {
                TextButton(onClick = onRemoveClick, enabled = !isProcessing) {
                    Text(text = stringResource(R.string.general_info_remove_photo), color = GashError_RED)
                }
            }
        }
    }
}