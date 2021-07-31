package com.ruben.epicworld.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val ColorPalette = lightColors(
    primary = PinkA400,
    primaryVariant = PinkA700,
    secondary = Black,
    onPrimary = White,
    onSecondary = White,
    background = Black,
    surface = Black,
    onBackground = White,
    onSurface = White,
)

@Composable
fun EpicWorldTheme(content: @Composable() () -> Unit) {
    val colors = ColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}