package com.ruben.epicworld.presentation.utility

import android.content.Context
import android.widget.Toast
import com.ruben.epicworld.domain.entity.gamedetails.DevelopersEntity
import com.ruben.epicworld.domain.entity.gamedetails.GenresEntity
import com.ruben.epicworld.domain.entity.gamedetails.PlatformsEntity
import com.ruben.epicworld.domain.entity.gamedetails.PublishersEntity
import com.ruben.epicworld.domain.entity.gamedetails.StoresEntity

/**
 * Created by Ruben Quadros on 06/08/21
 **/

object ApplicationUtility {
    fun getGenres(genres: List<GenresEntity>): String {
        var genresString = ""
        for (i in genres.indices) {
            genresString = "$genresString${genres[i].name}"
            if (i != genres.size - 1) {
                genresString = "$genresString "
            }
        }
        return genresString
    }

    fun getPlatforms(platforms: List<PlatformsEntity>): String {
        var platformsString = ""
        for (i in platforms.indices) {
            platformsString = "$platformsString${platforms[i].platform.name}"
            if (i != platforms.size - 1) {
                platformsString = "$platformsString, "
            }
        }
        return platformsString
    }

    fun getStores(stores: List<StoresEntity>): String {
        var storesString = ""
        for (i in stores.indices) {
            storesString = "$storesString${stores[i].store.name}"
            if (i != stores.size - 1) {
                storesString = "$storesString, "
            }
        }
        return storesString
    }

    fun getDevelopers(developers: List<DevelopersEntity>): String {
        var developersString = ""
        for (i in developers.indices) {
            developersString = "$developersString${developers[i].name}"
            if (i != developers.size - 1) {
                developersString = "$developersString, "
            }
        }
        return developersString
    }

    fun getPublishers(publishers: List<PublishersEntity>): String {
        var publishersString = ""
        for (i in publishers.indices) {
            publishersString = "$publishersString${publishers[i].name}"
            if (i != publishers.size - 1) {
                publishersString = "$publishersString, "
            }
        }
        return publishersString
    }
}

fun Context.showToast(message: String)  {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}