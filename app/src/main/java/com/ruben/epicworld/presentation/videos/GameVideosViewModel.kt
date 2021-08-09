package com.ruben.epicworld.presentation.videos

import androidx.lifecycle.viewModelScope
import com.ruben.epicworld.domain.interactor.GetGameVideosUseCase
import com.ruben.epicworld.presentation.base.BaseViewModel
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.videos.ui.GameVideosSideEffect
import com.ruben.epicworld.presentation.videos.ui.GameVideosState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 09/08/21
 **/
@HiltViewModel
class GameVideosViewModel @Inject constructor(
    private val getGameVideosUseCase: GetGameVideosUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<GameVideosState, GameVideosSideEffect>() {

    override fun createInitialState(): GameVideosState = GameVideosState(ScreenState.Loading, null, null)

    fun getGameVideos(gameId: Int) = intent {
        getGameVideosUseCase.invoke(
            viewModelScope,
            dispatcher,
            GetGameVideosUseCase.RequestValue(gameId)
        ) { record ->
            viewModelScope.launch {
                reduce {
                    if (record?.data != null) {
                        state.copy(
                            screenState = ScreenState.Success,
                            gameVideos = record.data,
                            error = null
                        )
                    } else {
                        state.copy(
                            screenState = ScreenState.Error,
                            gameVideos = null,
                            error = record?.error
                        )
                    }
                }
            }
        }
    }

    fun handleGameIdError() = intent {
        postSideEffect(GameVideosSideEffect.ShowGameIdErrorToast)
    }

    fun handleGameVideoError() = intent {
        postSideEffect(GameVideosSideEffect.ShowGameVideosErrorToast)
    }

    fun handleNoGameVideos() = intent {
        postSideEffect(GameVideosSideEffect.ShowNoGameVideosToast)
    }
}