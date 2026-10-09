package com.example.gash.core.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ProfileAvatar(
    imagePath: String?,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    placeholderContainerColor: Color = Color.White.copy(alpha = 0.25f),
    placeholderContentColor: Color = Color.White
) {
    val bitmap by produceState<ImageBitmap?>(initialValue = null, imagePath) {
        value = imagePath?.let { path ->
            withContext(Dispatchers.IO) { BitmapFactory.decodeFile(path)?.asImageBitmap() }
        }
    }

    val loaded = bitmap
    if (loaded != null) {
        Image(
            bitmap = loaded,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.size(size).clip(CircleShape)
        )
    } else {
        AvatarPlaceholder(
            modifier = modifier,
            size = size,
            containerColor = placeholderContainerColor,
            contentColor = placeholderContentColor
        )
    }
}