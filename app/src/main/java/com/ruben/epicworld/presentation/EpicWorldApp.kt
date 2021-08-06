package com.ruben.epicworld.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.ruben.epicworld.presentation.Destinations.GameDetails
import com.ruben.epicworld.presentation.Destinations.GameDetailsArgs.GameId
import com.ruben.epicworld.presentation.Destinations.GameDetailsArgs.GameScreenShots
import com.ruben.epicworld.presentation.Destinations.Home
import com.ruben.epicworld.presentation.details.ui.GameDetailsScreen
import com.ruben.epicworld.presentation.home.ui.HomeScreen

/**
 * Created by Ruben Quadros on 05/08/21
 **/
@ExperimentalFoundationApi
@Composable
fun EpicWorldApp() {
    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }
    NavHost(navController = navController, startDestination = Home) {
        composable(Home) {
            HomeScreen(
                openSearch = actions.openSearch,
                openFilters = actions.openFilter,
                openGameDetails = actions.openGameDetails
            )
        }
        composable(
            "$GameDetails/{$GameId}",
            arguments = listOf(
                navArgument(GameId) { type = NavType.IntType },
                navArgument(GameScreenShots) { type = NavType.StringArrayType }
            )
        ) {
            GameDetailsScreen(
                gameId = it.arguments?.getInt(GameId) ?: 0,
                gameScreenShots = navController.previousBackStackEntry?.arguments?.getStringArray(GameScreenShots)?.toList() ?: arrayListOf(),
                navigateBack = actions.navigateBack
            )
        }
    }
}