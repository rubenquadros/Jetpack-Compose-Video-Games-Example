package com.ruben.epicworld.presentation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.ruben.epicworld.presentation.Destinations.FiltersBottomSheet
import com.ruben.epicworld.presentation.Destinations.GameDetails
import com.ruben.epicworld.presentation.Destinations.GameDetailsArgs.GameId
import com.ruben.epicworld.presentation.Destinations.GameVideos
import com.ruben.epicworld.presentation.Destinations.GameVideosArgs.GameIdVideo
import com.ruben.epicworld.presentation.Destinations.Home
import com.ruben.epicworld.presentation.Destinations.Search
import com.ruben.epicworld.presentation.details.GameDetailsScreen
import com.ruben.epicworld.presentation.filters.GameFiltersScreen
import com.ruben.epicworld.presentation.home.HomeViewModel
import com.ruben.epicworld.presentation.home.ui.HomeScreen
import com.ruben.epicworld.presentation.search.GameSearchScreen
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import com.ruben.epicworld.presentation.videos.GameVideosScreen

/**
 * Created by Ruben Quadros on 05/08/21
 **/
@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun EpicWorldApp() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    val actions = remember(navController) { Actions(navController) }
    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetBackgroundColor = EpicWorldTheme.colors.background
    ) {
        //shared viewmodels
        val homeViewModel: HomeViewModel = hiltViewModel()

        NavHost(navController = navController, startDestination = Home) {
            composable(Home) {
                HomeScreen(
                    openSearch = actions.openSearch,
                    openFilters = actions.openFilter,
                    openGameDetails = actions.openGameDetails,
                    homeViewModel = homeViewModel
                )
            }

            composable(
                "$GameDetails/{$GameId}",
                arguments = listOf(
                    navArgument(GameId) { type = NavType.IntType }
                )
            ) {
                GameDetailsScreen(
                    navigateBack = actions.navigateBack,
                    openGameTrailer =  actions.openGameVideos
                )
            }

            composable(
                "$GameVideos/{${GameIdVideo}}",
                arguments = listOf(
                    navArgument(GameIdVideo) { type = NavType.IntType }
                )
            ) {
                GameVideosScreen(
                    navigateBack = actions.navigateBack
                )
            }

            composable(Search) {
                GameSearchScreen(
                    navigateToDetails = actions.openGameDetails,
                    navigateBack = actions.navigateBack
                )
            }

            bottomSheet(FiltersBottomSheet) {
                GameFiltersScreen()
            }
        }
    }
}