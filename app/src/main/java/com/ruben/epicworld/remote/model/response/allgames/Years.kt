package com.ruben.epicworld.remote.model.response.allgames

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.remote.model.response.allgames.Year

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class Years(
    @SerializedName("from")
    val from : Int,
    @SerializedName("to")
    val to : Int,
    @SerializedName("filter")
    val filter : String,
    @SerializedName("decade")
    val decade : Int,
    @SerializedName("years") val years : List<Year>,
)
