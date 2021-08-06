package com.ruben.epicworld.remote.model.response

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.games.StoresEntity

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class Stores(
    @SerializedName("id")
    val id : Int,
    @SerializedName("store")
    val store : Store
)

fun Stores.toEntity() = StoresEntity(id, store.toEntity())

fun List<Stores>.toEntity() = map { it.toEntity() }