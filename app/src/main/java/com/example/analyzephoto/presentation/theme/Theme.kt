package com.example.analyzephoto.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext

val BlackAndGoldTheme = darkColorScheme(
    primary = Gold,
    onPrimary = Black,
    secondary = DarkGold,
    onSecondary = Black,
    background = Black,
    onBackground = Gold,
    surface = Black,
    onSurface = Gold
)

val GoldGradient = Brush.horizontalGradient(
    colors = listOf(GradientStart, GradientEnd)
)

@Composable
fun AnalyzePhotoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BlackAndGoldTheme,
        content = content
    )
}