package com.ruben.epicworld.remote.model.response

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.games.ParentPlatformsEntity

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class ParentPlatforms(
    @SerializedName("platform")
    val platform : Platform
)

fun ParentPlatforms.toEntity() = ParentPlatformsEntity(platform.toEntity())

fun List<ParentPlatforms>.toEntity() = map { it.toEntity() }