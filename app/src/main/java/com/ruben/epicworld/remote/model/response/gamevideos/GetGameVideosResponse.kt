package com.ruben.epicworld.remote.model.response.gamevideos

import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import javax.annotation.concurrent.Immutable

/**
 * Created by Ruben Quadros on 09/08/21
 **/
@Immutable
@Stable
data class GetGameVideosResponse(
    @SerializedName("count")
    val count : Int,
    @SerializedName("next")
    val next : String,
    @SerializedName("previous")
    val previous : String,
    @SerializedName("results")
    val results : List<VideoResult>
)

fun GetGameVideosResponse.toEntity() = GameVideosEntity(count = count, results = results.toEntity())