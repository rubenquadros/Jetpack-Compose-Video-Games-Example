package com.ruben.epicworld.remote.model.response.common

import com.google.gson.annotations.SerializedName

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
) {
    constructor(): this(0, "", "")
}