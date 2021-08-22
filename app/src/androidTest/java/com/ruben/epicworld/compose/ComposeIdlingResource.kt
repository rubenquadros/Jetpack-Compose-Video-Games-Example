package com.ruben.epicworld.compose

import androidx.compose.ui.test.IdlingResource

/**
 * Created by Ruben Quadros on 21/08/21
 **/
class ComposeIdlingResource: IdlingResource {

    private var isAppIdle = false

    fun isAppIdle(isIdle: Boolean) {
        isAppIdle = isIdle
    }

    override val isIdleNow: Boolean
        get() = isAppIdle
}