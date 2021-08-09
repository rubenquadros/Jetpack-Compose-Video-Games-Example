package com.ruben.epicworld.remote.model.response.gamedetails

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.gamedetails.PublishersEntity

/**
 * Created by Ruben Quadros on 06/08/21
 **/
data class Publishers(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("slug")
    val slug : String,
    @SerializedName("games_count")
    val games_count : Int,
    @SerializedName("image_background")
    val imageBackground : String
)

fun Publishers.toEntity() = PublishersEntity(id, name, slug, games_count, imageBackground)

fun List<Publishers>.toEntity() = map { it.toEntity() }