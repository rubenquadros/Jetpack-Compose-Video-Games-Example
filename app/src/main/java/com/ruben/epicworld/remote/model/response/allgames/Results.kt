package com.ruben.epicworld.remote.model.response.allgames

import com.google.gson.annotations.SerializedName
import com.ruben.epicworld.domain.entity.games.GameResultEntity
import com.ruben.epicworld.remote.model.response.common.*

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class Results(
    @SerializedName("id")
    val id: Int,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("released")
    val released: String,
    @SerializedName("tba")
    val tba: Boolean,
    @SerializedName("background_image")
    val backgroundImage: String?,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("rating_top")
    val ratingTop: Int,
    @SerializedName("ratings")
    val ratings: List<Ratings>,
    @SerializedName("ratings_count")
    val ratingsCount: Int,
    @SerializedName("reviews_text_count")
    val reviewsTextCount: Int,
    @SerializedName("added")
    val added: Int,
    @SerializedName("added_by_status")
    val addedByStatus: AddedByStatus,
    @SerializedName("metacritic")
    val metaCritic: Int,
    @SerializedName("playtime")
    val playTime: Int,
    @SerializedName("suggestions_count")
    val suggestionsCount: Int,
    @SerializedName("updated")
    val updated: String,
    @SerializedName("user_game")
    val userGame: String?,
    @SerializedName("reviews_count")
    val reviewsCount: Int,
    @SerializedName("saturated_color")
    val saturatedColor: String,
    @SerializedName("dominant_color")
    val dominantColor: String,
    @SerializedName("platforms")
    val platforms: List<Platforms>,
    @SerializedName("parent_platforms")
    val parentPlatforms: List<ParentPlatforms>,
    @SerializedName("genres")
    val genres: List<Genres>,
    @SerializedName("stores")
    val stores: List<Stores>,
    @SerializedName("clip")
    val clip : String?,
    @SerializedName("tags")
    val tags : List<Tags>,
    @SerializedName("esrb_rating")
    val eSrbRating : EsrbRating?,
    @SerializedName("short_screenshots")
    val shortScreenshots : List<ShortScreenshots>
)

fun Results.toEntity() = GameResultEntity(id, name, backgroundImage ?: "", rating)

fun List<Results>.toEntity() = map { it.toEntity() }
