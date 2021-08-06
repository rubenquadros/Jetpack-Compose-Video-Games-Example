package com.ruben.epicworld.presentation.base

/**
 * Created by Ruben Quadros on 03/08/21
 **/
sealed class ScreenState {
    object Loading: ScreenState()
    object Success: ScreenState()
    object Error: ScreenState()
}
