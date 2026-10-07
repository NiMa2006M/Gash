package com.example.gash.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

private val GashLightColorScheme = lightColorScheme(
    primary = GashOrange,
    onPrimary = GashBackground_WHITE,

    secondary = GashGreen,
    onSecondary = GashBackground_WHITE,

    background = GashBackground_WHITE,
    onBackground = GashTextPrimary,

    surface = GashSurface,
    onSurface = GashTextPrimary,

    outline = GashBorder,

    error = GashError_RED
)

private val GashDarkColorScheme = darkColorScheme(
    primary = GashOrange,
    onPrimary = GashTextPrimary,

    secondary = GashGreen,
    onSecondary = GashBackground_WHITE,

    background = GashGreen,
    onBackground = GashBackground_WHITE,

    surface = GashGreen,
    onSurface = GashBackground_WHITE,

    outline = GashBorder,

    error = GashError_RED
)

@Composable
fun GashTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        GashDarkColorScheme
    } else {
        GashLightColorScheme
    }

    val density = LocalDensity.current

    val limitedDensity = Density(
        density = density.density,
        fontScale = density.fontScale.coerceAtMost(1.0f)
    )

    CompositionLocalProvider(
        LocalDensity provides limitedDensity
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}