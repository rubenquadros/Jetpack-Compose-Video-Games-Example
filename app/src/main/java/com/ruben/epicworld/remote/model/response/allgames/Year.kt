package com.ruben.epicworld.remote.model.response.allgames

import com.google.gson.annotations.SerializedName

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class Year(
    @SerializedName("year")
    val year: Int,
    @SerializedName("nofollow")
    val noFollow : Boolean,
    @SerializedName("count")
    val count : Int
)