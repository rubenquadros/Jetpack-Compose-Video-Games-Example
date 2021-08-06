package com.ruben.epicworld.remote.model.response

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.games.PlatformsEntity

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class Platforms(
    @SerializedName("platform")
    val platform : Platform,
    @SerializedName("released_at")
    val releasedAt : String?,
    @SerializedName("requirements_en")
    val requirementsEn : Requirements?,
    @SerializedName("requirements_ru")
    val requirementsRu : Requirements?
)

fun Platforms.toEntity() = PlatformsEntity(
    platform.toEntity(),
    releasedAt,
    requirementsEn?.toEntity(),
    requirementsRu?.toEntity()
)

fun List<Platforms>.toEntity() = map { it.toEntity() }
