package com.ruben.epicworld.remote.model.response

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.games.StoreEntity

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class Store(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("slug")
    val slug : String,
    @SerializedName("domain")
    val domain : String,
    @SerializedName("games_count")
    val gamesCount : Int,
    @SerializedName("image_background")
    val imageBackground : String
)

fun Store.toEntity() = StoreEntity(id, name, slug, domain, gamesCount, imageBackground)

fun List<Store>.toEntity() = map { it.toEntity() }
