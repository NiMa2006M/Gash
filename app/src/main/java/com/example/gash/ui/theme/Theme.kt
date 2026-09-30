package com.example.gash.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}