package com.ruben.epicworld.remote.model.response.allgames

import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName
import javax.annotation.concurrent.Immutable

/**
 * Created by Ruben Quadros on 01/08/21
 **/
@Immutable
@Stable
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
