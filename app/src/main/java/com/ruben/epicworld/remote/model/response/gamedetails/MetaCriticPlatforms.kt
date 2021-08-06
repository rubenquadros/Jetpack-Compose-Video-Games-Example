package com.ruben.epicworld.remote.model.response.gamedetails

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.remote.model.response.common.Platform

/**
 * Created by Ruben Quadros on 06/08/21
 **/
data class MetaCriticPlatforms(
    @SerializedName("metascore")
    val metaScore : Int,
    @SerializedName("url")
    val url : String,
    @SerializedName("platform")
    val platform : Platform
)
