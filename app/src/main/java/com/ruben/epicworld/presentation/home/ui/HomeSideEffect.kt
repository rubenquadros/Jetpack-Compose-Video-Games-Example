package com.ruben.epicworld.presentation.home.ui

/**
 * Created by Ruben Quadros on 03/08/21
 **/
sealed class HomeSideEffect {
    data class ShowSnackBar(val message: String, val action: String): HomeSideEffect()
}
