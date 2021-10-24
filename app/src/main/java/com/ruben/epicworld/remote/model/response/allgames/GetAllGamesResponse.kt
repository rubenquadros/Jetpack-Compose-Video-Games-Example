package com.ruben.epicworld.remote.model.response.allgames

import com.google.gson.annotations.SerializedName

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class GetAllGamesResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<Results>,
    @SerializedName("seo_title")
    val seoTitle: String,
    @SerializedName("seo_description")
    val seoDescription: String,
    @SerializedName("seo_keywords")
    val seoKeyWords: String,
    @SerializedName("seo_h1")
    val seoH1: String,
    @SerializedName("noindex")
    val noIndex: Boolean,
    @SerializedName("nofollow")
    val noFollow: Boolean,
    @SerializedName("description")
    val description: String,
    @SerializedName("filters")
    val filters: Filters,
    @SerializedName("nofollow_collections")
    val noFollowCollections: List<String>
)