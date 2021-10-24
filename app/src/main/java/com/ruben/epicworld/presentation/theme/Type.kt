package com.ruben.epicworld.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.ruben.epicworld.R
import javax.annotation.concurrent.Immutable

/**
 * Created by Ruben Quadros on 31/07/21
 **/

val PlayFair = FontFamily(
    Font(R.font.playfair_display_regular, FontWeight.Normal),
    Font(R.font.playfair_display_bold, FontWeight.Bold),
    Font(R.font.playfair_display_italic, FontWeight.Medium)
)

@Immutable
data class EpicWorldTypography(
    val title1: TextStyle,
    val title2: TextStyle,
    val title3: TextStyle,
    val subTitle1: TextStyle,
    val subTitle2: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val button: TextStyle
)

val LocalEpicWorldTypography = staticCompositionLocalOf {
    EpicWorldTypography(
        title1 = TextStyle.Default,
        title2 = TextStyle.Default,
        title3 = TextStyle.Default,
        subTitle1 = TextStyle.Default,
        subTitle2 = TextStyle.Default,
        body1 = TextStyle.Default,
        body2 = TextStyle.Default,
        button = TextStyle.Default
    )
}