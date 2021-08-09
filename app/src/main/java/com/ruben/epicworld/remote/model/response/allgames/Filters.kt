package com.ruben.epicworld.remote.model.response

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.remote.model.response.allgames.Years

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class Filters(
    @SerializedName("years")
    val years : List<Years>
)
