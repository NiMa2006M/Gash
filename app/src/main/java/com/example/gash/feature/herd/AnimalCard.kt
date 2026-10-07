package com.example.gash.feature.herd

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.domain.model.Animal
import com.example.gash.ui.theme.GashBorder
import com.example.gash.ui.theme.GashDanger_RED
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashSurface
import com.example.gash.ui.theme.GashTextPrimary
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun AnimalCard(
    animal: Animal,
    isFlipped: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onFlipBack: () -> Unit,
    onRemoveFromHerd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedRotationY by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 700
        ),
        label = "animal_card_flip"
    )
    val showBack = animatedRotationY >= 90f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationY = animatedRotationY
                    cameraDistance = 16f * density
                }
                .combinedClickable(
                    onClick = {
                        if (isFlipped) {
                            onFlipBack()
                        } else {
                            onClick()
                        }
                    },
                    onLongClick = onLongClick
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = GashSurface
            ),
            border = BorderStroke(
                width = 1.dp,
                color = GashBorder.copy(alpha = 0.6f)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp,
                pressedElevation = 6.dp
            )
        ) {
            if (!showBack) {
                AnimalCardFront(
                    animal = animal
                )
            } else {
                AnimalCardBack(
                    modifier = Modifier.graphicsLayer {
                        rotationY = 180f
                    },
                    onRemove = onRemoveFromHerd
                )
            }
        }
    }
}

@Composable
private fun AnimalCardFront(
    animal: Animal
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    GashGreen.copy(alpha = 0.12f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    R.drawable.icon_herd_management
                ),
                contentDescription = null,
                tint = GashGreen,
                modifier = Modifier.size(28.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(
                    R.string.animal_list_item_title,
                    animal.id
                ),
                color = GashTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            RfidBadge(
                rfidCode = animal.activeRfidCode
            )
        }
    }
}

@Composable
private fun AnimalCardBack(
    modifier: Modifier = Modifier,
    onRemove: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    GashDanger_RED.copy(alpha = 0.10f)
                )
                .clickable(
                    onClick = onRemove
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = GashDanger_RED,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = stringResource(
                R.string.herd_detail_culling_from_herd
            ),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RfidBadge(
    rfidCode: String?,
    modifier: Modifier = Modifier
) {
    val hasRfid = !rfidCode.isNullOrBlank()

    val badgeBg = if (hasRfid) {
        GashTextSecondary.copy(alpha = 0.08f)
    } else {
        Color(0xFFFFEBEE)
    }

    val textColor = if (hasRfid) {
        GashTextSecondary
    } else {
        GashDanger_RED
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(badgeBg)
            .padding(
                horizontal = 8.dp,
                vertical = 0.dp
            )
    ) {
        Text(
            text = rfidCode
                ?: stringResource(
                    R.string.animal_list_item_no_rfid
                ),
            color = textColor,
            fontSize = 10.sp,
            fontWeight = if (hasRfid) {
                FontWeight.Medium
            } else {
                FontWeight.Normal
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}