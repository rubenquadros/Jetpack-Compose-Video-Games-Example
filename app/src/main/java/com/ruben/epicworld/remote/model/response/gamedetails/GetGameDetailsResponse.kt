package com.ruben.epicworld.remote.model.response.gamedetails

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.remote.model.response.common.AddedByStatus
import com.ruben.epicworld.remote.model.response.common.EsrbRating
import com.ruben.epicworld.remote.model.response.common.Genres
import com.ruben.epicworld.remote.model.response.common.ParentPlatforms
import com.ruben.epicworld.remote.model.response.common.Platforms
import com.ruben.epicworld.remote.model.response.common.Ratings
import com.ruben.epicworld.remote.model.response.common.Stores
import com.ruben.epicworld.remote.model.response.common.Tags
import com.ruben.epicworld.remote.model.response.common.toEntity

/**
 * Created by Ruben Quadros on 06/08/21
 **/
data class GetGameDetailsResponse(
    @SerializedName("id")
    val id : Int,
    @SerializedName("slug")
    val slug : String,
    @SerializedName("name")
    val name : String,
    @SerializedName("name_original")
    val nameOriginal : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("metacritic")
    val metaCritic : Int,
    @SerializedName("metacritic_platforms")
    val metaCriticPlatforms : List<MetaCriticPlatforms>,
    @SerializedName("released")
    val released : String,
    @SerializedName("tba")
    val tba : Boolean,
    @SerializedName("updated")
    val updated : String,
    @SerializedName("background_image")
    val backgroundImage : String,
    @SerializedName("background_image_additional")
    val backgroundImageAdditional : String,
    @SerializedName("website")
    val website : String,
    @SerializedName("rating")
    val rating : Double,
    @SerializedName("rating_top")
    val ratingTop : Int,
    @SerializedName("ratings")
    val ratings : List<Ratings>,
    @SerializedName("reactions")
    val reactions : Reactions,
    @SerializedName("added")
    val added : Int,
    @SerializedName("added_by_status")
    val addedByStatus : AddedByStatus,
    @SerializedName("playtime")
    val playtime : Int,
    @SerializedName("screenshots_count")
    val screenshotsCount : Int,
    @SerializedName("movies_count")
    val moviesCount : Int,
    @SerializedName("creators_count")
    val creatorsCount : Int,
    @SerializedName("achievements_count")
    val achievementsCount : Int,
    @SerializedName("parent_achievements_count")
    val parentAchievementsCount : Int,
    @SerializedName("reddit_url")
    val redditUrl : String,
    @SerializedName("reddit_name")
    val redditName : String,
    @SerializedName("reddit_description")
    val redditDescription : String,
    @SerializedName("reddit_logo")
    val redditLogo : String,
    @SerializedName("reddit_count")
    val redditCount : Int,
    @SerializedName("twitch_count")
    val twitchCount : Int,
    @SerializedName("youtube_count")
    val youtubeCount : Int,
    @SerializedName("reviews_text_count")
    val reviewsTextCount : Int,
    @SerializedName("ratings_count")
    val ratingsCount : Int,
    @SerializedName("suggestions_count")
    val suggestionsCount : Int,
    @SerializedName("alternative_names")
    val alternativeNames : List<String>,
    @SerializedName("metacritic_url")
    val metaCriticUrl : String,
    @SerializedName("parents_count")
    val parentsCount : Int,
    @SerializedName("additions_count")
    val additionsCount : Int,
    @SerializedName("game_series_count")
    val gameSeriesCount : Int,
    @SerializedName("user_game")
    val userGame : String,
    @SerializedName("reviews_count")
    val reviewsCount : Int,
    @SerializedName("saturated_color")
    val saturatedColor : String,
    @SerializedName("dominant_color")
    val dominantColor : String,
    @SerializedName("parent_platforms")
    val parentPlatforms : List<ParentPlatforms>,
    @SerializedName("platforms")
    val platforms : List<Platforms>,
    @SerializedName("stores")
    val stores : List<Stores>,
    @SerializedName("developers")
    val developers : List<Developers>,
    @SerializedName("genres")
    val genres : List<Genres>,
    @SerializedName("tags")
    val tags : List<Tags>,
    @SerializedName("publishers")
    val publishers : List<Publishers>,
    @SerializedName("esrb_rating")
    val esrbRating : EsrbRating,
    @SerializedName("clip")
    val clip : String,
    @SerializedName("description_raw")
    val descriptionRaw : String
)

fun GetGameDetailsResponse.toEntity() = GameDetailsEntity(
    id = id,
    name = name,
    description = description,
    rating = rating,
    released = released,
    backgroundImage = backgroundImage,
    parentPlatformsEntities = parentPlatforms.toEntity(),
    storesEntities = stores.toEntity(),
    developersEntities = developers.toEntity(),
    genresEntities = genres.toEntity(),
    publishersEntities = publishers.toEntity()
)
