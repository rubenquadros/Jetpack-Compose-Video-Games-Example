package com.ruben.epicworld.remote.model.response

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.games.RatingsEntity

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class Ratings(
    @SerializedName("id")
    val id : Int,
    @SerializedName("title")
    val title : String,
    @SerializedName("count")
    val count : Int,
    @SerializedName("percent")
    val percent : Double
)

fun Ratings.toEntity() = RatingsEntity(id, title, count, percent)

fun List<Ratings>.toEntity() = map { it.toEntity() }