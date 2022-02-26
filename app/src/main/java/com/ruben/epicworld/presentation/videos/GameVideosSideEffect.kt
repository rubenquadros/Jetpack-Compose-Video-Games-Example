package com.ruben.epicworld.presentation.videos

/**
 * Created by Ruben Quadros on 09/08/21
 **/
sealed class GameVideosSideEffect {
    object ShowGameIdErrorToast: GameVideosSideEffect()
    object GameVideosError: GameVideosSideEffect()
    object ShowNoGameVideosToast: GameVideosSideEffect()
}