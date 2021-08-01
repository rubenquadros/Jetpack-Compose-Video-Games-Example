package com.ruben.epicworld.presentation.home.ui

import com.ruben.epicworld.presentation.base.ViewIntent

/**
 * Created by Ruben Quadros on 01/08/21
 **/
sealed class HomeIntent: ViewIntent {
    object AllGames: HomeIntent()
}
