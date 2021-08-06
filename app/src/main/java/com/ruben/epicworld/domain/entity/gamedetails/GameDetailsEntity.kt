package com.ruben.epicworld.domain.entity.gamedetails

/**
 * Created by Ruben Quadros on 06/08/21
 **/
data class GameDetailsEntity(
    val id : Int,
    val name: String,
    val description: String,
    val rating: Double,
    val released: String,
    val backgroundImage: String,
    val parentPlatformsEntities: List<ParentPlatformsEntity>,
    val storesEntities: List<StoresEntity>,
    val developersEntities: List<DevelopersEntity>,
    val genresEntities: List<GenresEntity>,
    val publishersEntities: List<PublishersEntity>
) {
    constructor(): this(0, "", "", 0.0, "", "", arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf())
}
