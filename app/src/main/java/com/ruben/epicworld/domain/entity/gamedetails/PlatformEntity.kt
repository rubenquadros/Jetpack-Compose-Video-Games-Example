package com.ruben.epicworld.domain.entity.gamedetails

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class PlatformEntity(
    val id : Int,
    val name : String,
    val slug : String,
    val image: String?,
    val yearEnd: String?,
    val yearStart: String?,
    val gamesCount: Int,
    val imageBackground: String?
)