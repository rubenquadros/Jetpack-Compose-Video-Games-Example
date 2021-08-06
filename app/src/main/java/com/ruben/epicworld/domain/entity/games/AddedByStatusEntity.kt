package com.ruben.epicworld.domain.entity.games

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class AddedByStatusEntity(
    val yet : Int,
    val owned : Int,
    val beaten : Int,
    val toPlay : Int,
    val dropped : Int,
    val playing : Int
)
