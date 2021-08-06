package com.ruben.epicworld.remote.model.response.common

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.gamedetails.RequirementsEntity

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class Requirements(
    @SerializedName("minimum")
    val minimum: String?,
    @SerializedName("recommended")
    val recommended: String?
)

fun Requirements.toEntity() = RequirementsEntity(minimum, recommended)