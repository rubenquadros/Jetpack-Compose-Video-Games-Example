package com.ruben.epicworld.remote.model.response.allgames

import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName
import javax.annotation.concurrent.Immutable

/**
 * Created by Ruben Quadros on 01/08/21
 **/
@Immutable
@Stable
data class Filters(
    @SerializedName("years")
    val years : List<Years>
)
