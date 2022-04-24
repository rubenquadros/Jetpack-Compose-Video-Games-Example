package com.ruben.epicworld.domain.entity.gamedetails

import androidx.compose.runtime.Stable
import javax.annotation.concurrent.Immutable

/**
 * Created by Ruben Quadros on 06/08/21
 **/
@Immutable
@Stable
data class GameDetailsEntity(
    val id : Int,
    val name: String,
    val description: String,
    val rating: Double,
    val released: String,
    val backgroundImage: String,
    val moviesCount: Int,
    val parentPlatformsEntity: List<ParentPlatformsEntity>,
    val platformsEntity: List<PlatformsEntity>,
    val storesEntity: List<StoresEntity>,
    val developersEntity: List<DevelopersEntity>,
    val genresEntity: List<GenresEntity>,
    val publishersEntity: List<PublishersEntity>
) {
    constructor(): this(0, "", "", 0.0, "", "", 0,  arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf())
}
