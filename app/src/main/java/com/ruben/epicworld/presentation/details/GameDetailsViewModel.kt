package com.ruben.epicworld.presentation.details

import androidx.lifecycle.SavedStateHandle
import com.ruben.epicworld.domain.interactor.GetGameDetailsUseCase
import com.ruben.epicworld.presentation.Destinations.GameDetailsArgs.GameId
import com.ruben.epicworld.presentation.base.BaseViewModel
import com.ruben.epicworld.presentation.base.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 06/08/21
 **/
@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGameDetailsUseCase: GetGameDetailsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<GameDetailsState, GameDetailsSideEffect>(savedStateHandle) {

    override fun createInitialState(): GameDetailsState = GameDetailsState(ScreenState.Loading, null, null)

    override fun initData() {
        super.initData()
        val gameId: Int? = savedStateHandle.get(GameId)
        if (gameId == null || gameId == 0) {
            handleGameIdError()
        } else {
            getGameDetails(gameId = gameId)
        }
    }

    fun getGameDetails(gameId: Int) = intent {
        getGameDetailsUseCase.invoke(
            dispatcher,
            GetGameDetailsUseCase.RequestValue(gameId = gameId)
        ).collect { record ->
            reduce {
                if (record.data != null) {
                    state.copy(
                        screenState = ScreenState.Success,
                        gameDetails = record.data,
                        error = null
                    )
                } else {
                    handleError()
                    state.copy(
                        screenState = ScreenState.Error,
                        gameDetails = null,
                        error = record.error
                    )
                }
            }
        }
    }

    fun handleGameIdError() = intent {
        postSideEffect(GameDetailsSideEffect.ShowGameIdErrorToast)
    }

    private fun handleError() = intent {
        postSideEffect(GameDetailsSideEffect.ShowGameDetailsErrorToast)
    }
}