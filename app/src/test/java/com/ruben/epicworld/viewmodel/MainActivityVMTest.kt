package com.ruben.epicworld.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.ruben.epicworld.presentation.activity.MainActivityViewModel
import com.ruben.epicworld.presentation.activity.MainState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test

/**
 * Created by Ruben Quadros on 09/07/22
 **/
@OptIn(ExperimentalCoroutinesApi::class)
class MainActivityVMTest {
    private val mainActivityViewModel = MainActivityViewModel(SavedStateHandle())

    @Test
    fun `when full screen is initiated the state should be set to full screen`() =
        runTest(UnconfinedTestDispatcher()) {
            val initialState = MainState()
            val testSubject = mainActivityViewModel.test(initialState = initialState)

            testSubject.testIntent {
                toggleFullScreen(isFullScreen = true)
            }.assert(initialState = initialState) {
                states({ copy(isFullScreen = true) })
            }
        }

    @Test
    fun `when full screen is exited the full state should be set to false`() =
        runTest(UnconfinedTestDispatcher()) {
            val initialState = MainState(isFullScreen = true)
            val testSubject = mainActivityViewModel.test(initialState = initialState)

            testSubject.testIntent {
                toggleFullScreen(isFullScreen = false)
            }.assert(initialState = initialState) {
                states({ copy(isFullScreen = false) })
            }
        }
}