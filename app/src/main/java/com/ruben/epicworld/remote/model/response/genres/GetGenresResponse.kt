package com.ruben.epicworld.remote.model.response.genres


import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.remote.model.response.common.Genres

data class GetGenresResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: Any,
    @SerializedName("previous")
    val previous: Any,
    @SerializedName("results")
    val results: List<Genres>
)