package com.ruben.epicworld.remote.model.response.common

import com.google.gson.annotations.SerializedName

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class AddedByStatus(
    @SerializedName("yet")
    val yet : Int,
    @SerializedName("owned")
    val owned : Int,
    @SerializedName("beaten")
    val beaten : Int,
    @SerializedName("toplay")
    val toPlay : Int,
    @SerializedName("dropped")
    val dropped : Int,
    @SerializedName("playing")
    val playing : Int
) {
    constructor(): this(0, 0, 0, 0, 0, 0)
}
