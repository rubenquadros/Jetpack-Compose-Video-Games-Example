package com.ruben.epicworld.presentation.home.ui

import androidx.paging.PagingData
import com.ruben.epicworld.domain.entity.games.GameResultsEntity
import com.ruben.epicworld.presentation.base.ViewState
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ruben Quadros on 01/08/21
 **/
sealed class HomeState: ViewState {

    object InitialState: HomeState()
    object LoadingState: HomeState()
    class AllGamesData(val games: Flow<PagingData<GameResultsEntity>>): HomeState()
}
