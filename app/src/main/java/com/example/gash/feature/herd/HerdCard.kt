package com.example.gash.feature.herd

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.domain.model.Herd
import com.example.gash.ui.theme.GashBorder
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashSurface
import com.example.gash.ui.theme.GashTextPrimary
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun HerdCard(
    herd: Herd,
    onClick: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = GashSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = GashBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 8.dp,
                    top = 16.dp,
                    bottom = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Herd icon
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(GashGreen.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    tint = GashGreen,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Herd information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = herd.name,
                    color = GashTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = stringResource(
                        R.string.herd_management_animal_count,
                        herd.animalCount
                    ),
                    color = GashTextSecondary,
                    fontSize = 13.sp
                )
            }

            // More menu
            Box {
                IconButton(
                    onClick = {
                        isMenuExpanded = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(
                            R.string.common_btn_more
                        ),
                        tint = GashTextSecondary
                    )
                }

                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = {
                        isMenuExpanded = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(
                                    R.string.herd_menu_rename
                                )
                            )
                        },
                        onClick = {
                            isMenuExpanded = false
                            onRenameClick()
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(
                                    R.string.herd_menu_delete
                                )
                            )
                        },
                        onClick = {
                            isMenuExpanded = false
                            onDeleteClick()
                        }
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                contentDescription = null,
                tint = GashTextSecondary.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}