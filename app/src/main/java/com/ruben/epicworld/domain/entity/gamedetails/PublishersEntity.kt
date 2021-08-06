package com.ruben.epicworld.domain.entity.gamedetails

/**
 * Created by Ruben Quadros on 06/08/21
 **/
data class PublishersEntity(
    val id : Int,
    val name : String,
    val slug : String,
    val games_count : Int,
    val imageBackground : String
)
