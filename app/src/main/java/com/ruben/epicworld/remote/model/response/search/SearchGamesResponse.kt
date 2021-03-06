package com.ruben.epicworld.remote.model.response.search


import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.remote.model.response.allgames.Results
import javax.annotation.concurrent.Immutable

@Immutable
@Stable
data class SearchGamesResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<Results>,
    @SerializedName("user_platforms")
    val userPlatforms: Boolean
)