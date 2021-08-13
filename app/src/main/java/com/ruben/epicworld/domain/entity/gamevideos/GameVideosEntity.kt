package com.ruben.epicworld.domain.entity.gamevideos

/**
 * Created by Ruben Quadros on 09/08/21
 **/
data class GameVideosEntity(
    val count: Int,
    val results: List<VideoResultEntity>
) {
    constructor(): this(0, arrayListOf())
}
