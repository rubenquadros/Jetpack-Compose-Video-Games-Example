package com.ruben.epicworld.presentation

import android.os.Bundle
import androidx.navigation.NavHostController
import com.ruben.epicworld.presentation.Destinations.Filters
import com.ruben.epicworld.presentation.Destinations.GameDetails
import com.ruben.epicworld.presentation.Destinations.GameDetailsArgs.GameScreenShots
import com.ruben.epicworld.presentation.Destinations.Search

/**
 * Created by Ruben Quadros on 05/08/21
 **/
object Destinations {
    const val Home = "home"
    const val GameDetails = "gameDetails"
    const val Search = "search"
    const val Filters = "filters"

    object GameDetailsArgs {
        const val GameId = "gameId"
        const val GameScreenShots = "gameScreenShots"
    }
}

class Actions(navHostController: NavHostController) {
    val openSearch: () -> Unit = {
        navHostController.navigate(Search)
    }

    val openFilter: () -> Unit = {
        navHostController.navigate(Filters)
    }

    val openGameDetails: (Int, Array<String>) -> Unit = { gameId, gameScreenShots ->
        navHostController.currentBackStackEntry?.arguments = Bundle().apply {
            putStringArray(GameScreenShots, gameScreenShots)
        }
        navHostController.navigate("$GameDetails/$gameId")
    }

    val navigateBack: () -> Unit = {
        navHostController.popBackStack()
    }
}