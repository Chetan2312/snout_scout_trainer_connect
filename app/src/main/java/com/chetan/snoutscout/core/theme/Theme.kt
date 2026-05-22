package com.chetan.snoutscout.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = SoftOlive,
    onPrimary = CreamWhite,
    secondary = WarmBrown,
    onSecondary = CreamWhite,
    background = CreamWhite,
    onBackground = InkBlack,
    surface = WarmBeige,
    onSurface = InkBlack,
    error = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkOlive,
    onPrimary = InkBlack,
    secondary = DarkBrown,
    onSecondary = InkBlack,
    background = DarkBackground,
    onBackground = LightText,
    surface = DarkSurface,
    onSurface = LightText,
    error = ErrorRed
)

@Composable
fun SnoutScoutTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = AppTypography,
        content = content
    )
}