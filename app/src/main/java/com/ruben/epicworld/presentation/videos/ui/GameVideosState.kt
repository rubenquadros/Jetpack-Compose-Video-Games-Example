package com.ruben.epicworld.presentation.videos.ui

import com.ruben.epicworld.domain.entity.base.ErrorRecord
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import com.ruben.epicworld.presentation.base.ScreenState

/**
 * Created by Ruben Quadros on 09/08/21
 **/
data class GameVideosState(
    val screenState: ScreenState,
    val gameVideos: GameVideosEntity?,
    val error: ErrorRecord?
)
