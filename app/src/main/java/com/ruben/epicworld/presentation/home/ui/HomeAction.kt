package com.ruben.epicworld.presentation.home.ui

import com.ruben.epicworld.presentation.base.ViewAction

/**
 * Created by Ruben Quadros on 01/08/21
 **/
sealed class HomeAction: ViewAction {
    object GetAllGames: HomeAction()
}
