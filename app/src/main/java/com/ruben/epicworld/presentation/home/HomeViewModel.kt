package com.ruben.epicworld.presentation.home

import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ruben.epicworld.domain.entity.base.ErrorRecord
import com.ruben.epicworld.domain.entity.games.GameResultEntity
import com.ruben.epicworld.domain.interactor.GamesSource
import com.ruben.epicworld.presentation.base.BaseViewModel
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.home.ui.HomeSideEffect
import com.ruben.epicworld.presentation.home.ui.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 01/08/21
 **/
@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gamesSource: GamesSource
) : BaseViewModel<HomeState, HomeSideEffect>(savedStateHandle) {

    override fun createInitialState(): HomeState = HomeState(ScreenState.Loading, null, null)

    override fun initData() = intent {
        val result = getAllGames()
        reduce {
            state.copy(screenState = ScreenState.Success, games = result.flow, error = null)
        }
    }

    fun handlePaginationDataError() = intent {
        reduce {
            state.copy(screenState = ScreenState.Error,
                games = null,
                error = ErrorRecord.ServerError
            )
        }
    }

    fun handlePaginationAppendError(message: String, action: String) = intent {
        postSideEffect(HomeSideEffect.ShowSnackBar(message = message, action = action))
    }

    private fun getAllGames(): Pager<Int, GameResultEntity> {
        return Pager(PagingConfig(50)) {
            gamesSource
        }
    }
}