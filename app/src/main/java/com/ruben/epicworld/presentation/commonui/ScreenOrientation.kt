package com.ruben.epicworld.presentation.commonui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.ruben.epicworld.presentation.utility.findActivity

/**
 * Created by Ruben Quadros on 25/06/22
 **/
@Composable
fun ScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(key1 = Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {  }
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation

        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}