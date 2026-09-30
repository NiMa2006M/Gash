package com.example.gash.feature.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.gash.R
import com.example.gash.ui.theme.GashGreen
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun WelcomeScreen(
    onFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(5000.milliseconds)
        onFinished()
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(GashGreen)
        ) {
            val screenWidth = maxWidth.value
            val screenHeight = maxHeight.value

            Image(
                painter = painterResource(R.drawable.gash_welcome_patternup),
                contentDescription = null,
                modifier = Modifier
                    .size(width = (screenWidth * 1.42f).dp, height = (screenHeight * 0.57f).dp)
                    .align(Alignment.TopEnd)
                    .offset(
                        x = (screenWidth * 0.30f).dp,
                        y = (0).dp
                    ),
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(R.drawable.gash_welcome_patterndown),
                contentDescription = null,
                modifier = Modifier
                    .size(width = (screenWidth * 1.42f).dp, height = (screenHeight * 0.57f).dp)
                    .align(Alignment.BottomStart)
                    .offset(
                        x = (-(screenWidth * 0.30f)).dp,
                        y = (0).dp
                    ),
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(R.drawable.gash_welcome_cream),
                contentDescription = null,
                modifier = Modifier
                    .size(width = (screenWidth * 1.25).dp, height = (screenHeight * 0.50f).dp)
                    .align(Alignment.BottomCenter)
                    .offset(
                        x = (0).dp,
                        y = (screenHeight * 0.125f).dp
                    ),
                contentScale = ContentScale.Fit
            )

            AnimatedAnimalHead(
                imageRes = R.drawable.gash_welcome_cow,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(width = (screenWidth * 0.50).dp, height = (screenHeight * 0.22f).dp)
                    .offset(
                        x = (-screenWidth * 0.025f).dp,
                        y = (0).dp
                    ),
                rotationAmount = 2.5f,
                verticalMovement = 2.5.dp,
                animationDuration  = 2400
            )

            AnimatedAnimalHead(
                imageRes = R.drawable.gash_welcome_goat,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(width = (screenWidth * 0.54).dp, height = (screenHeight * 0.22f).dp)
                    .offset(
                        x = (-screenWidth * 0.05f).dp,
                        y = (0).dp
                    ),
                rotationAmount = 2f,
                verticalMovement = 1.5.dp,
                animationDuration = 3500,
                delayMillis = 1800
            )

            AnimatedAnimalHead(
                imageRes = R.drawable.gash_welcome_sheep,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(width = (screenWidth * 0.54).dp, height = (screenHeight * 0.22f).dp)
                    .offset(
                        x = ((screenWidth * 0.10f)).dp,
                        y = (0).dp
                    ),
                rotationAmount = 2f,
                verticalMovement = 1.5.dp,
                animationDuration = 3000,
                delayMillis = 900
            )


            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(R.drawable.gash_icon_colorless),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .width((screenWidth * 0.30f).dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = stringResource(R.string.app_slogan),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width((screenWidth * 0.65f).dp)
                )
            }
        }
    }

}