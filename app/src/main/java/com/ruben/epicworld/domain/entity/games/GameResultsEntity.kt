package com.ruben.epicworld.domain.entity.games

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class GameResultsEntity(
    val id: Int,
    val slug: String,
    val name: String,
    val released: String,
    val tba: Boolean,
    val backgroundImage: String,
    val rating: Double,
    val ratingTop: Int,
    val ratingsCount: Int,
    val ratings: List<RatingsEntity>,
    val reviewsTextCount: Int,
    val added: Int,
    val addedByStatus: AddedByStatusEntity,
    val metaCritic: Int,
    val playTime: Int,
    val suggestionsCount: Int,
    val updated: String,
    val userGame: String?,
    val reviewsCount: Int,
    val saturatedColor: String,
    val dominantColor: String,
    val platforms: List<PlatformsEntity>,
    val parentPlatforms: List<ParentPlatformsEntity>,
    val genres: List<GenresEntity>,
    val stores: List<StoresEntity>,
    val clip : String?,
    val tags : List<TagsEntity>,
    val eSrbRating : EsrbRatingEntity?,
    val shortScreenshots : List<ShortScreenshotsEntity>
)
