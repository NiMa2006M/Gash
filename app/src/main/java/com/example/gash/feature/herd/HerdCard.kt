package com.example.gash.feature.herd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.domain.model.Herd
import com.example.gash.ui.theme.GashFieldBackground
import com.example.gash.ui.theme.GashTextPrimary
import com.example.gash.ui.theme.GashTextSecondary

@Composable
fun HerdCard(
    herd: Herd,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GashFieldBackground, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = herd.name, color = GashTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(
                text = stringResource(R.string.herd_management_animal_count, herd.animalCount),
                color = GashTextSecondary,
                fontSize = 12.sp
            )
        }

        Box {
            IconButton(onClick = { isMenuExpanded = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = null)
            }
            DropdownMenu(expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.herd_menu_rename)) },
                    onClick = {
                        isMenuExpanded = false
                        onRenameClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.herd_menu_delete)) },
                    onClick = {
                        isMenuExpanded = false
                        onDeleteClick()
                    }
                )
            }
        }
    }
}