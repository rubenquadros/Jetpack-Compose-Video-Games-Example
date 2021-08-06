package com.ruben.epicworld.remote.model.response

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.games.TagsEntity

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class Tags(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("slug")
    val slug : String,
    @SerializedName("language")
    val language : String,
    @SerializedName("games_count")
    val gamesCount : Int,
    @SerializedName("image_background")
    val imageBackground : String
)

fun Tags.toEntity() = TagsEntity(id, name, slug, language, gamesCount, imageBackground)

fun List<Tags>.toEntity() = map { it.toEntity() }