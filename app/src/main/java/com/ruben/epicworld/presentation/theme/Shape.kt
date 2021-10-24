package com.ruben.epicworld.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import javax.annotation.concurrent.Immutable

/**
 * Created by Ruben Quadros on 31/07/21
 **/
@Immutable
data class EpicWorldShapes(
    val smallRoundCornerShape: Shape,
    val mediumRoundCornerShape: Shape,
    val largeRoundCornerShape: Shape,
)

val LocalEpicWorldShapes = staticCompositionLocalOf {
    EpicWorldShapes(
        smallRoundCornerShape = RoundedCornerShape(ZeroCornerSize),
        mediumRoundCornerShape = RoundedCornerShape(ZeroCornerSize),
        largeRoundCornerShape = RoundedCornerShape(ZeroCornerSize)
    )
}