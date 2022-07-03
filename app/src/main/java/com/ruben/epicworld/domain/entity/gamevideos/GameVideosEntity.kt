package com.ruben.epicworld.domain.entity.gamevideos

import androidx.compose.runtime.Stable
import javax.annotation.concurrent.Immutable

/**
 * Created by Ruben Quadros on 09/08/21
 **/
@Immutable
@Stable
data class GameVideosEntity(
    val count: Int,
    val results: List<VideoResultEntity>
) {
    constructor(): this(0, emptyList())
}
