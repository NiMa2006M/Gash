package com.example.gash.feature.animal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.ui.theme.GashError_RED
import com.example.gash.ui.theme.GashFieldBackground
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashOrange
import com.example.gash.ui.theme.GashTextPrimary
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun AnimalDetailScreen(
    uiState: AnimalDetailUiState,
    onBack: () -> Unit,
    onRecordWeightClick: () -> Unit,
    onDismissRecordWeight: () -> Unit,
    onConfirmRecordWeight: (Double) -> Unit,
    onMoveHerdClick: () -> Unit,
    onDismissMoveHerd: () -> Unit,
    onHerdSelected: (Long) -> Unit,
    onExpireClick: () -> Unit,
    onDismissExpireConfirm: () -> Unit,
    onConfirmExpire: () -> Unit,
    onRestoreClick: () -> Unit
) {
    val animal = uiState.animal
    val isExpired = animal?.expiredAt != null

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GashGreen, RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_btn_back), tint = Color.White)
                    }
                }

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    AnimalAvatar(animalId = animal?.id ?: 0)
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.animal_list_item_title, animal?.id ?: 0),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(6.dp))
                        Row {
                            StatusChip(
                                text = if (isExpired) stringResource(R.string.animal_detail_status_expired)
                                else stringResource(R.string.animal_detail_status_active),
                                background = if (isExpired) GashError_RED.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.25f)
                            )
                            Spacer(Modifier.width(8.dp))
                            StatusChip(text = animal?.activeRfidCode?.let { "RFID: $it" } ?: stringResource(R.string.animal_list_item_no_rfid))
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-16).dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatTile(
                    value = uiState.weightHistory.maxByOrNull { it.recordedAt }?.weightKg?.let { "%.1f".format(it) } ?: "-",
                    label = stringResource(R.string.animal_detail_latest_weight),
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    value = uiState.herd?.name ?: stringResource(R.string.animal_detail_no_herd),
                    label = stringResource(R.string.animal_detail_herd_label),
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    value = if (isExpired) stringResource(R.string.animal_detail_status_expired) else stringResource(R.string.animal_detail_status_active),
                    label = stringResource(R.string.animal_detail_status_label),
                    modifier = Modifier.weight(1f)
                )
            }

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.animal_detail_weight_history_title),
                        fontWeight = FontWeight.Bold,
                        color = GashTextPrimary
                    )
                    OutlinedButton(onClick = onRecordWeightClick) {
                        Text(stringResource(R.string.animal_detail_record_weight_button))
                    }
                }

                Spacer(Modifier.height(8.dp))

                WeightHistoryChart(
                    history = uiState.weightHistory,
                    modifier = Modifier.background(GashFieldBackground, RoundedCornerShape(14.dp))
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onMoveHerdClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.animal_detail_move_herd_button))
            }

            if (isExpired) {
                Button(
                    onClick = onRestoreClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = GashGreen)
                ) {
                    Text(stringResource(R.string.animal_detail_restore_button))
                }
            } else {
                Button(
                    onClick = onExpireClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = GashError_RED)
                ) {
                    Text(stringResource(R.string.animal_detail_expire_button))
                }
            }
        }
    }

    if (uiState.isMoveHerdSheetOpen) {
        MoveHerdSheet(
            herds = uiState.availableHerds,
            currentHerdId = animal?.herdId,
            onHerdSelected = onHerdSelected,
            onDismiss = onDismissMoveHerd
        )
    }

    if (uiState.isRecordWeightSheetOpen) {
        RecordWeightSheet(
            error = uiState.error,
            isSubmitting = uiState.isSubmitting,
            onConfirm = onConfirmRecordWeight,
            onDismiss = onDismissRecordWeight
        )
    }

    if (uiState.isExpireConfirmOpen) {
        ExpireConfirmDialog(onConfirm = onConfirmExpire, onDismiss = onDismissExpireConfirm)
    }
}

@Composable
private fun AnimalAvatar(animalId: Long) {
    Box(
        modifier = Modifier.size(56.dp).background(Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "#$animalId", color = GashGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
private fun StatusChip(text: String, background: Color = Color.White.copy(alpha = 0.2f)) {
    Box(
        modifier = Modifier.background(background, RoundedCornerShape(10.dp)).padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = text, color = Color.White, fontSize = 11.sp)
    }
}