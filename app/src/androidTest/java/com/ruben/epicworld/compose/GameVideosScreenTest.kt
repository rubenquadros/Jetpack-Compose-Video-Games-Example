package com.ruben.epicworld.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import com.ruben.epicworld.domain.entity.base.ErrorRecord
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.videos.GameVideosScreen
import com.ruben.epicworld.presentation.videos.GameVideosSideEffect
import com.ruben.epicworld.presentation.videos.GameVideosState
import com.ruben.epicworld.presentation.videos.GameVideosViewModel
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
        every { gameVideosViewModel.createInitialState() } answers {
            GameVideosState(ScreenState.Loading, null, null)
        }
        every { gameVideosViewModel.uiSideEffect() } answers { flow { } }
        every { gameVideosViewModel.initData() } answers { }
        every { gameVideosViewModel.handleGameIdError() } answers { }
        every { gameVideosViewModel.handleNoGameVideos() } answers { }
    }

    @Test
    fun loader_should_be_shown_when_fetching_game_videos() {
        every { gameVideosViewModel.uiState() } answers { MutableStateFlow(gameVideosViewModel.createInitialState()) }
        composeTestRule.setContent {
            GameVideosScreen(navigateBack = { }, gameVideosViewModel = gameVideosViewModel, onFullScreenToggle = {})
        }
        composeTestRule.onNodeWithTag("ProgressBar").assertIsDisplayed()
    }

    @Test
    fun providing_invalid_game_id_should_show_toast_and_exit_screen() {
        every { gameVideosViewModel.uiState() } answers { MutableStateFlow(gameVideosViewModel.createInitialState()) }
        every { gameVideosViewModel.uiSideEffect() } answers {
            flow { emit(GameVideosSideEffect.ShowGameIdErrorToast) }
        }
        var navigateBack = false
        composeTestRule.setContent {
            GameVideosScreen(navigateBack = { navigateBack = true }, gameVideosViewModel = gameVideosViewModel, onFullScreenToggle = {})
        }
        Assert.assertTrue(navigateBack)
    }

    @Test
    fun should_start_playing_trailer_after_getting_game_videos() {
        every { gameVideosViewModel.uiState() } answers {
            MutableStateFlow(GameVideosState(ScreenState.Success, FakeGamesData.getFakeGameVideos(), null))
        }
        composeTestRule.setContent {
            GameVideosScreen(navigateBack = { }, gameVideosViewModel = gameVideosViewModel, onFullScreenToggle = {})
        }
        composeTestRule.onNodeWithTag("VideoPlayer").assertIsDisplayed()
    }

    @Test
    fun should_display_current_playing_game_video_title() {
        every { gameVideosViewModel.uiState() } answers {
            MutableStateFlow(GameVideosState(ScreenState.Success, FakeGamesData.getFakeGameVideos(), null))
        }
        composeTestRule.setContent {
            GameVideosScreen(navigateBack = { }, gameVideosViewModel = gameVideosViewModel, onFullScreenToggle = {})
        }
        composeTestRule.onAllNodesWithText("GTA Online: Smuggler's Run Trailer").assertCountEquals(2)
    }

    @Test
    fun should_show_all_videos_as_a_playlist() {
        every { gameVideosViewModel.uiState() } answers {
            MutableStateFlow(GameVideosState(ScreenState.Success, FakeGamesData.getFakeGameVideos(), null))
        }
        composeTestRule.setContent {
            GameVideosScreen(navigateBack = { }, gameVideosViewModel = gameVideosViewModel, onFullScreenToggle = {})
        }
        composeTestRule.onAllNodesWithContentDescription("Game Trailer").assertCountEquals(3)
        composeTestRule.onNodeWithContentDescription("Play Game Trailer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Now Playing").assertIsDisplayed()
    }

    @Test
    fun game_play_list_should_be_scrollable() {
        every { gameVideosViewModel.uiState() } answers {
            MutableStateFlow(GameVideosState(ScreenState.Success, FakeGamesData.getFakeGameVideos(), null))
        }
        composeTestRule.setContent {
            GameVideosScreen(navigateBack = { }, gameVideosViewModel = gameVideosViewModel, onFullScreenToggle = {})
        }
        composeTestRule.onNodeWithText("GTA Online: Tiny Racers Trailer").performScrollTo()
        composeTestRule.onNodeWithText("GTA Online: Tiny Racers Trailer").assertIsDisplayed()
    }

    @Test
    fun game_videos_in_playlist_should_be_clickable() {
        every { gameVideosViewModel.uiState() } answers {
            MutableStateFlow(GameVideosState(ScreenState.Success, FakeGamesData.getFakeGameVideos(), null))
        }
        composeTestRule.setContent {
            GameVideosScreen(navigateBack = { }, gameVideosViewModel = gameVideosViewModel, onFullScreenToggle = {})
        }
        composeTestRule.onAllNodesWithTag("TrailerParent").assertCountEquals(3)[0].assertHasClickAction()
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
            GameVideosScreen(navigateBack = { navigateBack = true }, gameVideosViewModel = gameVideosViewModel, onFullScreenToggle = {})
        }
        Assert.assertTrue(navigateBack)
    }

    @Test
    fun getting_error_on_fetching_game_videos_should_show_toast_and_exit_screen() {
        every { gameVideosViewModel.uiState() } answers {
            MutableStateFlow(GameVideosState(ScreenState.Error, null, ErrorRecord.ServerError))
        }
        every { gameVideosViewModel.uiSideEffect() } answers {
            flow { emit(GameVideosSideEffect.GameVideosError) }
        }
        var navigateBack = false
        composeTestRule.setContent {
            GameVideosScreen(navigateBack = { navigateBack = true }, gameVideosViewModel = gameVideosViewModel, onFullScreenToggle = {})
        }
        Assert.assertTrue(navigateBack)
    }

}