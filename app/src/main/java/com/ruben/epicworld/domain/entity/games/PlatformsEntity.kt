package com.ruben.epicworld.domain.entity.games

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class PlatformsEntity(
    val platform : PlatformEntity,
    val releasedAt : String?,
    val requirementsEn : RequirementsEntity?,
    val requirementsRu : RequirementsEntity?
)
