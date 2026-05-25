package com.snoutscout.app.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = SnoutScoutColors.Primary,
    onPrimary = SnoutScoutColors.OnPrimary,
    primaryContainer = SnoutScoutColors.SurfaceAlt,
    secondary = SnoutScoutColors.Secondary,
    onSecondary = SnoutScoutColors.OnSecondary,
    secondaryContainer = SnoutScoutColors.SurfaceAlt,
    tertiary = SnoutScoutColors.Accent,
    background = SnoutScoutColors.Background,
    surface = SnoutScoutColors.Surface,
    surfaceVariant = SnoutScoutColors.SurfaceAlt,
    onBackground = SnoutScoutColors.Text,
    onSurface = SnoutScoutColors.Text,
    onSurfaceVariant = SnoutScoutColors.TextSecondary,
    outline = SnoutScoutColors.Border,
    error = SnoutScoutColors.Error
)

private val DarkColorScheme = darkColorScheme(
    primary = SnoutScoutColors.PrimaryLight,
    onPrimary = SnoutScoutColors.OnPrimary,
    primaryContainer = SnoutScoutColors.DarkSurfaceAlt,
    secondary = SnoutScoutColors.SecondaryLight,
    onSecondary = SnoutScoutColors.OnSecondary,
    background = SnoutScoutColors.DarkBackground,
    surface = SnoutScoutColors.DarkSurface,
    surfaceVariant = SnoutScoutColors.DarkSurfaceAlt,
    onBackground = SnoutScoutColors.DarkText,
    onSurface = SnoutScoutColors.DarkText,
    onSurfaceVariant = SnoutScoutColors.DarkTextSecondary,
    outline = SnoutScoutColors.DarkBorder,
    error = SnoutScoutColors.Error
)

@Composable
fun SnoutScoutTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = SnoutScoutTypography,
        shapes = SnoutScoutShapes,
        content = content
    )
}
