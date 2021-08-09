package com.ruben.epicworld.remote.model.response.gamevideos

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.gamevideos.VideoResultEntity

/**
 * Created by Ruben Quadros on 09/08/21
 **/
data class VideoResult(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("preview")
    val preview : String,
    @SerializedName("data")
    val data : VideoData
)

fun VideoResult.toEntity() = VideoResultEntity(preview = preview, name = name, video = data.toEntity())

fun List<VideoResult>.toEntity() = map { it.toEntity() }
