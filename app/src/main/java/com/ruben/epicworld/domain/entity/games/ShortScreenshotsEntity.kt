package com.ruben.epicworld.domain.entity.games

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class ShortScreenshotsEntity(
    val id: Int,
    val image: String
)

fun ShortScreenshotsEntity.toStringArray() = image

fun List<ShortScreenshotsEntity>.toStringArray() = map { it.toStringArray() }