package com.ruben.epicworld.remote.model.response.gamevideos

import com.google.gson.annotations.SerializedName

/**
 * Created by Ruben Quadros on 09/08/21
 **/
data class VideoData(
    @SerializedName("480")
    val medium : String,
    @SerializedName("max")
    val max : String
)

fun VideoData.toEntity() = max
