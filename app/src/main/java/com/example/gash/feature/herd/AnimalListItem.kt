package com.example.gash.feature.herd

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun AnimalListItem(
    animal: Animal,
    isFlipped: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onFlipBack: () -> Unit,
    onRemoveFromHerd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotationY by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "animal_list_item_flip"
    )

    val showBack = rotationY >= 90f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp)
            .graphicsLayer {
                this.rotationY = rotationY
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
        shape = RoundedCornerShape(18.dp),
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
        if (showBack) {
            AnimalListItemBack(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        this.rotationY = 180f
                    },
                onRemove = onRemoveFromHerd
            )
        } else {
            AnimalListItemFront(
                animal = animal
            )
        }
    }
}

@Composable
private fun AnimalListItemFront(
    animal: Animal
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(GashGreen.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_herd_management),
                contentDescription = null,
                tint = GashGreen,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = animal.activeRfidCode
                    ?.takeIf { it.isNotBlank() }
                    ?: stringResource(R.string.animal_list_item_no_rfid),
                color = if (animal.activeRfidCode.isNullOrBlank()) {
                    GashDanger_RED
                } else {
                    GashTextSecondary
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(GashTextSecondary.copy(alpha = 0.08f))
                    .padding(horizontal = 10.dp, vertical = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        R.string.animal_list_item_title,
                        animal.id
                    ),
                    color = GashTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun AnimalListItemBack(
    modifier: Modifier = Modifier,
    onRemove: () -> Unit
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(
                R.string.herd_detail_culling_from_herd
            ),
            tint = GashDanger_RED,
            modifier = Modifier
                .size(28.dp)
                .combinedClickable(
                    onClick = onRemove,
                    onLongClick = onRemove
                )
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = stringResource(
                R.string.herd_detail_culling_from_herd
            ),
            color = GashDanger_RED,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
//@Composable
//private fun RfidBadge(
//    rfidCode: String?,
//    modifier: Modifier = Modifier
//) {
//    val hasRfid = !rfidCode.isNullOrBlank()
//
//    Box(
//        modifier = modifier
//            .clip(RoundedCornerShape(8.dp))
//            .background(
//                if (hasRfid) {
//                    GashGreen.copy(alpha = 0.12f)
//                } else {
//                    Color(0xFFFFEBEE)
//                }
//            )
//            .padding(
//                horizontal = 8.dp,
//                vertical = 3.dp
//            )
//    ) {
//        Text(
//            text = rfidCode
//                ?.takeIf { it.isNotBlank() }
//                ?: stringResource(
//                    R.string.animal_list_item_no_rfid
//                ),
//            color = if (hasRfid) {
//                GashGreen
//            } else {
//                GashDanger_RED
//            },
//            fontSize = 15.sp,
//            fontWeight = FontWeight.Bold,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//    }
//}