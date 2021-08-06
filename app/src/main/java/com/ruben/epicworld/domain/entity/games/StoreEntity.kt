package com.ruben.epicworld.domain.entity.games

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class StoreEntity(
    val id : Int,
    val name : String,
    val slug : String,
    val domain : String,
    val gamesCount : Int,
    val imageBackground : String
)
