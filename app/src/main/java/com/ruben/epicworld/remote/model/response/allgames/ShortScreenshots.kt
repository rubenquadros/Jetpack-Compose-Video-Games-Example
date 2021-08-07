package com.ruben.epicworld.remote.model.response.allgames

import com.google.gson.annotations.SerializedName

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class ShortScreenshots(
    @SerializedName("id")
    val id : Int,
    @SerializedName("image")
    val image : String
)