package com.ruben.epicworld.domain.entity.games

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class GenresEntity(
    val id : Int,
    val name : String,
    val slug : String,
    val gamesCount : Int,
    val imageBackground : String
)
