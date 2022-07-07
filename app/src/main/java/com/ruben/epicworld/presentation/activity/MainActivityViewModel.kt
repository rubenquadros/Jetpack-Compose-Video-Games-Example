package com.ruben.epicworld.presentation.activity

import androidx.lifecycle.SavedStateHandle
import com.ruben.epicworld.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 07/07/22
 **/
@HiltViewModel
class MainActivityViewModel @Inject constructor(handle: SavedStateHandle) :
    BaseViewModel<MainState, Nothing>(handle) {

    override fun createInitialState() =  MainState()

    fun toggleFullScreen(isFullScreen: Boolean) = intent {
        reduce {
            if (isFullScreen) {
                state.copy(isFullScreen = true)
            } else {
                state.copy(isFullScreen = false)
            }
        }
    }
}