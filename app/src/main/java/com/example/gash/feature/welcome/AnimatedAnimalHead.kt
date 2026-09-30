package com.example.gash.feature.welcome

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun AnimatedAnimalHead(
    imageRes: Int,
    modifier: Modifier = Modifier,
    rotationAmount: Float = 2f,
    verticalMovement: Dp = 1.5.dp,
    animationDuration: Int = 3000,
    delayMillis: Int = 0
) {

    val infiniteTransition = rememberInfiniteTransition(
        label = "animal_head"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = -rotationAmount,
        targetValue = rotationAmount,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                delayMillis = delayMillis,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse        ),
        label = "animal_rotation"
    )

    val verticalOffset by infiniteTransition.animateFloat(
        initialValue = -verticalMovement.value,
        targetValue = verticalMovement.value,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration + 500,
                delayMillis = delayMillis + 300,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animal_vertical"
    )

    Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        modifier = modifier.graphicsLayer {
            rotationZ = rotation
            translationY = verticalOffset
        },
        contentScale = ContentScale.Fit
    )
}