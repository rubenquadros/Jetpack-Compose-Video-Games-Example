package com.ruben.epicworld.presentation.home

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ruben.epicworld.domain.interactor.GamesSource
import com.ruben.epicworld.presentation.base.BaseViewModel
import com.ruben.epicworld.presentation.home.ui.HomeAction
import com.ruben.epicworld.presentation.home.ui.HomeIntent
import com.ruben.epicworld.presentation.home.ui.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 01/08/21
 **/
@HiltViewModel
class HomeViewModel @Inject constructor(private val gamesSource: GamesSource): BaseViewModel<HomeIntent, HomeAction, HomeState>() {

    override fun createInitialState(): HomeState = HomeState.InitialState

    override fun mapIntentToAction(intent: HomeIntent): HomeAction {
        return when(intent) {
            is HomeIntent.AllGames -> HomeAction.GetAllGames
        }
    }

    override fun handleAction(action: HomeAction) {
        when (action) {
            is HomeAction.GetAllGames -> getAllGames()
        }
    }

    private fun getAllGames() {
        val games = Pager(PagingConfig(50)) {
            gamesSource
        }
        setState(HomeState.AllGamesData(games = games.flow))
    }
}