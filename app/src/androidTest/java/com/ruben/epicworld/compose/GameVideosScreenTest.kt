package com.ruben.epicworld.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.ruben.epicworld.domain.entity.base.ErrorRecord
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.videos.GameVideosViewModel
import com.ruben.epicworld.presentation.videos.ui.GameVideosScreen
import com.ruben.epicworld.presentation.videos.ui.GameVideosSideEffect
import com.ruben.epicworld.presentation.videos.ui.GameVideosState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Ruben Quadros on 13/08/21
 **/
@ExperimentalAnimationApi
class GameVideosScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val gameVideosViewModel = mockk<GameVideosViewModel>()

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
        every { gameVideosViewModel.initData() } answers { }
        every { gameVideosViewModel.uiSideEffect() } answers { flow { } }
        every { gameVideosViewModel.uiState() } answers { MutableStateFlow(GameVideosState(ScreenState.Loading, null, null)) }
        every { gameVideosViewModel.getGameVideos(any()) } answers { }
        every { gameVideosViewModel.handleGameIdError() } answers { }
        every { gameVideosViewModel.handleNoGameVideos() } answers { }
        every { gameVideosViewModel.handleGameVideoError() } answers { }
    }

    @Test
    fun loader_should_be_shown_when_fetching_game_videos() {
        composeTestRule.setContent {
            GameVideosScreen(gameId = 123, navigateBack = { }, gameVideosViewModel = gameVideosViewModel)
        }
        composeTestRule.onNodeWithTag("ProgressBar").assertIsDisplayed()
    }

    @Test
    fun providing_invalid_game_id_should_show_toast_and_exit_screen() {
        every { gameVideosViewModel.uiSideEffect() } answers {
            flow { emit(GameVideosSideEffect.ShowGameIdErrorToast) }
        }
        var navigateBack = false
        composeTestRule.setContent {
            GameVideosScreen(gameId = 0, navigateBack = { navigateBack = true }, gameVideosViewModel = gameVideosViewModel)
        }
        Assert.assertTrue(navigateBack)
    }

    @Test
    fun getting_no_videos_for_the_game_should_show_toast_and_exit_screen() {
        every { gameVideosViewModel.uiState() } answers {
            MutableStateFlow(GameVideosState(ScreenState.Success, GameVideosEntity(), null))
        }
        every { gameVideosViewModel.uiSideEffect() } answers {
            flow { emit(GameVideosSideEffect.ShowNoGameVideosToast) }
        }
        var navigateBack = false
        composeTestRule.setContent {
            GameVideosScreen(gameId = 123, navigateBack = { navigateBack = true }, gameVideosViewModel = gameVideosViewModel)
        }
        Assert.assertTrue(navigateBack)
    }

    @Test
    fun getting_error_on_fetching_game_videos_should_show_toast_and_exit_screen() {
        every { gameVideosViewModel.uiState() } answers {
            MutableStateFlow(GameVideosState(ScreenState.Error, null, ErrorRecord.ServerError))
        }
        every { gameVideosViewModel.uiSideEffect() } answers {
            flow { emit(GameVideosSideEffect.ShowGameVideosErrorToast) }
        }
        var navigateBack = false
        composeTestRule.setContent {
            GameVideosScreen(gameId = 123, navigateBack = { navigateBack = true }, gameVideosViewModel = gameVideosViewModel)
        }
        Assert.assertTrue(navigateBack)
    }

}