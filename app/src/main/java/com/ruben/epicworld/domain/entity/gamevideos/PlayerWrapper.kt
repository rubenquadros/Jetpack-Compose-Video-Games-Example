package com.ruben.epicworld.domain.entity.gamevideos

import androidx.compose.runtime.Stable
import com.google.android.exoplayer2.Player
import javax.annotation.concurrent.Immutable

/**
 * Created by Ruben Quadros on 03/07/22
 **/
@Immutable
@Stable
data class PlayerWrapper(
    val exoPlayer: Player
)
