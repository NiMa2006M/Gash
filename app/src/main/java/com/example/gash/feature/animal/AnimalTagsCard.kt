package com.example.gash.feature.animal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.domain.model.AnimalTags
import com.example.gash.domain.model.TagConfig
import com.example.gash.ui.theme.GashBorder
import com.example.gash.ui.theme.GashOrange
import com.example.gash.ui.theme.GashTextPrimary
import com.example.gash.ui.theme.GashTextSecondary
import java.util.Vector
import kotlin.contracts.contract

@Composable
fun AnimalTagsCard(
    tagConfigs: List<TagConfig>,
    tags: AnimalTags,
    canEdit: Boolean,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.animal_detail_tags_title),
                fontWeight = FontWeight.Bold,
                color = GashTextPrimary
            )
            if (canEdit) {
                OutlinedButton(onClick = onEditClick, colors = ButtonDefaults.buttonColors(containerColor = GashOrange, contentColor = Color.White)) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = stringResource(R.string.common_btn_edit))
                    Spacer(Modifier.width(4.dp))
                    Text(text = stringResource(R.string.common_btn_edit))
                }
            }
        }

        tagConfigs.forEachIndexed { index, config ->
            val label = config.name.ifBlank {
                stringResource(R.string.settings_tag_default_name, config.slot)
            }
            val value = tags.valueFor(config.slot)

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = label, color = GashTextSecondary, fontSize = 13.sp)
                Text(
                    text = value ?: stringResource(R.string.animal_detail_tag_not_set),
                    color = if (value == null) GashTextSecondary else GashTextPrimary,
                    fontWeight = if (value == null) FontWeight.Normal else FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
            if (index < tagConfigs.lastIndex) {
                HorizontalDivider(color = GashBorder)
            }
        }
    }
}