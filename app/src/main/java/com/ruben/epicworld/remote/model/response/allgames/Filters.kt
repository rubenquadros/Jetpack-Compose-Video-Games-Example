package com.ruben.epicworld.remote.model.response.allgames

import com.google.gson.annotations.SerializedName

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class Filters(
    @SerializedName("years")
    val years : List<Years>
)
