package com.ruben.epicworld.presentation.details

import com.ruben.epicworld.domain.entity.base.ErrorRecord
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.presentation.base.ScreenState

/**
 * Created by Ruben Quadros on 06/08/21
 **/
data class GameDetailsState(
    val screenState: ScreenState,
    val gameDetails: GameDetailsEntity?,
    val error: ErrorRecord?
)
