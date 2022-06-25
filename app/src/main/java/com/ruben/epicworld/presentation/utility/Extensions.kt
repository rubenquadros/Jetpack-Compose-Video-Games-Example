package com.ruben.epicworld.presentation.utility

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Created by Ruben Quadros on 25/06/22
 **/
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}