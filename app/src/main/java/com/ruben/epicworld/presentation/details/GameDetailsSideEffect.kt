package com.ruben.epicworld.presentation.details

/**
 * Created by Ruben Quadros on 06/08/21
 **/
sealed class GameDetailsSideEffect {
    object ShowGameIdErrorToast: GameDetailsSideEffect()
    object ShowGameDetailsErrorToast: GameDetailsSideEffect()
}
