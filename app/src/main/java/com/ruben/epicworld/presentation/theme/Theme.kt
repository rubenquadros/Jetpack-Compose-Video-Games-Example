package com.ruben.epicworld.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

/**
 * Created by Ruben Quadros on 31/07/21
 **/

private val ColorPalette = lightColors(
    primary = PinkA400,
    primaryVariant = PinkA700,
    secondary = Black,
    onPrimary = White,
    onSecondary = White,
    background = White,
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