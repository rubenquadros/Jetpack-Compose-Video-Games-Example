package com.ruben.epicworld.presentation.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ruben.epicworld.R

/**
 * Created by Ruben Quadros on 31/07/21
 **/

// Set of Material typography styles to start with

val PlayFair = FontFamily(
    Font(R.font.playfair_display_regular, FontWeight.Normal),
    Font(R.font.playfair_display_bold, FontWeight.Bold),
    Font(R.font.playfair_display_italic, FontWeight.Medium)
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    h2 = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    h3 = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    h4 = TextStyle(
      fontFamily = PlayFair,
      fontWeight = FontWeight.Normal,
      fontSize = 18.sp
    ),
    body1 = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Black
    ),
    button = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
)