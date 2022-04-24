package com.ruben.epicworld.domain.entity.games

import androidx.compose.runtime.Stable
import javax.annotation.concurrent.Immutable

/**
 * Created by Ruben Quadros on 01/08/21
 **/
@Immutable
@Stable
data class GamesEntity(
    val gameEntities: List<GameResultEntity>
) {
    constructor(): this(arrayListOf())
}
