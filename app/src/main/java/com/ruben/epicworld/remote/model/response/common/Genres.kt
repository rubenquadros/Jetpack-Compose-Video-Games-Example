package com.ruben.epicworld.remote.model.response.common

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.genres.GenresResultEntity

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class Genres(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("slug")
    val slug : String,
    @SerializedName("games_count")
    val gamesCount : Int,
    @SerializedName("image_background")
    val imageBackground : String
)

fun Genres.toEntity() = GenresResultEntity(id, name, slug, gamesCount, imageBackground)

fun List<Genres>.toEntity() = map { it.toEntity() }