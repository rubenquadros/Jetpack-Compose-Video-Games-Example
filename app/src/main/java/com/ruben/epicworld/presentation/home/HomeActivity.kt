package com.ruben.epicworld.presentation.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import com.ruben.epicworld.presentation.base.BaseActivity
import com.ruben.epicworld.presentation.home.ui.HomeIntent
import com.ruben.epicworld.presentation.home.ui.HomeScreen
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by Ruben Quadros on 31/07/21
 **/
@ExperimentalFoundationApi
@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EpicWorldTheme {
                HomeScreen(
                    homeViewModel = homeViewModel,
                    searchClick = { handleSearchClick() },
                    filterClick = { handleFilterClick() },
                    gameClick = { handleGameClick() }
                )
            }
        }
        dispatchGetGamesIntent()
    }

    private fun dispatchGetGamesIntent() {
        homeViewModel.dispatchIntent(HomeIntent.AllGames)
    }

    private fun handleGameClick() {
        //implement game click functionality
    }

    private fun handleSearchClick() {
        //implement search functionality
    }

    private fun handleFilterClick() {
        //implement filter functionality
    }
}