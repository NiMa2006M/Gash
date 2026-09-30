package com.example.gash.feature.home

import android.R.attr.maxHeight
import android.R.attr.maxWidth
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gash.R
import com.example.gash.core.ui.components.AvatarPlaceholder
import com.example.gash.ui.theme.GashGreen

@Composable
fun HomeHeader(
    uiState: HomeUiState,
    currentPage: HomePage,
    onPageSelected: (HomePage) -> Unit
) {
    val headerShape = RoundedCornerShape(
        bottomStart = 28.dp,
        bottomEnd = 28.dp
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(headerShape)
            .background(GashGreen)
    ) {

        val screenWidth = maxWidth
        val screenHeight = maxHeight

        // Pattern Layer
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Box(
                modifier = Modifier.matchParentSize()
            ) {

                Image(
                    painter = painterResource(
                        R.drawable.gash_home_patternup
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .width(320.dp)
                        .height(280.dp),
                    contentScale = ContentScale.FillBounds

                )

                Image(
                    painter = painterResource(
                        R.drawable.gash_home_patterndown
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .width(280.dp)
                        .height(245.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        // Header Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    horizontal = 20.dp,
                    vertical = 20.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AvatarPlaceholder()
                    Spacer(Modifier.width(10.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = uiState.farmName,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = stringResource(R.string.home_device_reference, uiState.deviceReferenceCode),
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                }

                Image(
                    painter = painterResource(R.drawable.gash_icon_colorless),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.home_total_animals_count, uiState.totalAnimalCount),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.home_total_animals_label),
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                HomePage.entries.forEach { page ->
                    HomeShortcutButton(
                        page = page,
                        selected = page == currentPage,
                        onClick = { onPageSelected(page) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
