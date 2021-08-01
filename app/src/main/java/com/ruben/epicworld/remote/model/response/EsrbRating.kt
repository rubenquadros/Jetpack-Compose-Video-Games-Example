package com.ruben.epicworld.remote.model.response

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.games.EsrbRatingEntity

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class EsrbRating(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("slug")
    val slug : String
)

fun EsrbRating.toEntity() = EsrbRatingEntity(id, name, slug)