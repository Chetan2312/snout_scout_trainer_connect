package com.snoutscout.app.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val SnoutScoutTypography = Typography(
    displayLarge = TextStyle(fontWeight = FontWeight.W800, fontSize = 32.sp, letterSpacing = (-0.5).sp),
    headlineLarge = TextStyle(fontWeight = FontWeight.W800, fontSize = 28.sp, letterSpacing = (-0.3).sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.W700, fontSize = 22.sp, letterSpacing = (-0.3).sp),
    headlineSmall = TextStyle(fontWeight = FontWeight.W700, fontSize = 18.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.W700, fontSize = 16.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.W600, fontSize = 15.sp),
    titleSmall = TextStyle(fontWeight = FontWeight.W600, fontSize = 14.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.W400, fontSize = 15.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.W400, fontSize = 14.sp, lineHeight = 22.sp),
    bodySmall = TextStyle(fontWeight = FontWeight.W400, fontSize = 13.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.W600, fontSize = 14.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.W600, fontSize = 13.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.W600, fontSize = 11.sp)
)
