package com.ruben.epicworld.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created by Ruben Quadros on 31/07/21
 **/
@Composable
fun EpicWorldTheme(content: @Composable () -> Unit) {
    val localEpicWorldColors = EpicWorldColors(
        primary = PrimaryColor,
        primaryVariant = PrimaryVariantColor,
        secondary = SecondaryColor,
        background = Color.Black,
        onBackground = Color.White,
        surface = SurfaceColor,
        onDisabled = DisabledColor
    )

    val localEpicWorldTypography = EpicWorldTypography(
        title1 = TextStyle(
            fontFamily = PlayFair,
            fontSize = 24.sp,
            lineHeight = 28.8.sp,
            fontWeight = FontWeight.W700
        ),
        title2 = TextStyle(
            fontFamily = PlayFair,
            fontSize = 22.sp,
            lineHeight = 26.4.sp,
            fontWeight = FontWeight.W700
        ),
        title3 = TextStyle(
            fontFamily = PlayFair,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.W700
        ),
        subTitle1 = TextStyle(
            fontFamily = PlayFair,
            fontSize = 18.sp,
            lineHeight = 21.6.sp,
            fontWeight = FontWeight.W700
        ),
        subTitle2 = TextStyle(
            fontFamily = PlayFair,
            fontSize = 16.sp,
            lineHeight = 19.2.sp,
            fontWeight = FontWeight.W700
        ),
        body1 = TextStyle(
            fontFamily = PlayFair,
            fontSize = 18.sp,
            lineHeight = 21.6.sp,
            fontWeight = FontWeight.W400
        ),
        body2 = TextStyle(
            fontFamily = PlayFair,
            fontSize = 16.sp,
            lineHeight = 19.2.sp,
            fontWeight = FontWeight.W400
        ),
        button = TextStyle(
            fontFamily = PlayFair,
            fontSize = 18.sp,
            lineHeight = 21.6.sp,
            fontWeight = FontWeight.W700
        )
    )

    val localEpicWorldShapes = EpicWorldShapes(
        smallRoundCornerShape = RoundedCornerShape(4.dp),
        mediumRoundCornerShape = RoundedCornerShape(6.dp),
        largeRoundCornerShape = RoundedCornerShape(8.dp)
    )

    CompositionLocalProvider(
        LocalEpicWorldColors provides localEpicWorldColors,
        LocalEpicWorldShapes provides localEpicWorldShapes,
        LocalEpicWorldTypography provides localEpicWorldTypography
    ) {
        MaterialTheme(content = content)
    }
}

object EpicWorldTheme {
    val colors: EpicWorldColors
        @Composable
        @ReadOnlyComposable
        @NonRestartableComposable
        get() = LocalEpicWorldColors.current

    val shapes: EpicWorldShapes
        @Composable
        @ReadOnlyComposable
        @NonRestartableComposable
        get() = LocalEpicWorldShapes.current

    val typography: EpicWorldTypography
        @Composable
        @ReadOnlyComposable
        @NonRestartableComposable
        get() = LocalEpicWorldTypography.current
}