package com.ruben.epicworld.domain.entity.games

import androidx.compose.runtime.Stable
import javax.annotation.concurrent.Immutable

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class GameResultEntity(
    val id: Int,
    val name: String,
    val backgroundImage: String,
    val rating: Double,
)

@Immutable
@Stable
data class GameResultsEntity(
    val gameResults: List<GameResultEntity>
) {
    constructor(): this(emptyList())
}