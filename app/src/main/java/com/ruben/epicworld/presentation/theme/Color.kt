package com.ruben.epicworld.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import javax.annotation.concurrent.Immutable

/**
 * Created by Ruben Quadros on 31/07/21
 **/

val PrimaryColor = Color(0xFFF50057)
val PrimaryVariantColor = Color(0xFFFF80AB)
val SecondaryColor = Color(0xFFFFC400)
val SurfaceColor = Color(0xFFE0E0E0)
val DisabledColor = Color(0xFF9E9E9E)

@Immutable
data class EpicWorldColors(
    val primary: Color,
    val primaryVariant: Color,
    val secondary: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onDisabled: Color
)

val LocalEpicWorldColors = staticCompositionLocalOf {
    EpicWorldColors(
        primary = Color.Red,
        primaryVariant = Color.Magenta,
        secondary = Color.Yellow,
        background = Color.DarkGray,
        onBackground = Color.Green,
        surface = Color.Gray,
        onDisabled = Color.LightGray
    )
}