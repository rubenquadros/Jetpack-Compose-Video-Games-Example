package com.ruben.epicworld.compose

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.details.GameDetailsViewModel
import com.ruben.epicworld.presentation.details.ui.GameDetailsScreen
import com.ruben.epicworld.presentation.details.ui.GameDetailsSideEffect
import com.ruben.epicworld.presentation.details.ui.GameDetailsState
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
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
 * Created by Ruben Quadros on 07/08/21
 **/
class GameDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val gameDetailsViewModel = mockk<GameDetailsViewModel>()

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
        every { gameDetailsViewModel.initData() } answers {  }
        every { gameDetailsViewModel.uiSideEffect() } answers  { flow {  } }
        every { gameDetailsViewModel.uiState() } answers { MutableStateFlow(GameDetailsState(ScreenState.Loading, null, null)) }
        every { gameDetailsViewModel.getGameDetails(any()) } answers { }
        every { gameDetailsViewModel.handleGameIdError() } answers { }
    }

    @Test
    fun loader_should_be_shown_when_data_is_being_fetched() {
        composeTestRule.setContent {
            EpicWorldTheme {
                GameDetailsScreen(gameId = 123, navigateBack = { }, openGameTrailer = { }, gameDetailsViewModel)
            }
        }
        composeTestRule.onNodeWithTag("ProgressBar").assertIsDisplayed()
    }

    @Test
    fun providing_invalid_game_id_should_show_toast_and_exit_the_screen() {
        every { gameDetailsViewModel.uiSideEffect() } answers  {
            flow { emit(GameDetailsSideEffect.ShowGameIdErrorToast) }
        }
        var navigateBack = false
        composeTestRule.setContent {
            EpicWorldTheme {
                GameDetailsScreen(gameId = 0, navigateBack = { navigateBack = true }, openGameTrailer = { }, gameDetailsViewModel)
            }
        }
        Assert.assertTrue(navigateBack)
    }

    @Test
    fun game_details_should_be_shown_once_fetched() {
        every { gameDetailsViewModel.uiState() } answers { MutableStateFlow(GameDetailsState(ScreenState.Success, FakeGamesData.getFakeGameDetails(), null)) }
        composeTestRule.setContent {
            EpicWorldTheme {
                GameDetailsScreen(gameId = 123, navigateBack = { }, openGameTrailer = { }, gameDetailsViewModel)
            }
        }
        //Game image should be shown
        composeTestRule.onNodeWithContentDescription("Game Screenshots").assertIsDisplayed()

        //Trailer play button should be shown and clickable
        composeTestRule.onNodeWithContentDescription("Play Trailer").assertIsDisplayed().assertHasClickAction()

        //Game title should be shown
        composeTestRule.onNodeWithText("Max Payne").assertIsDisplayed()

        //Game genres should be shown
        composeTestRule.onNodeWithText("Action Shooting").assertIsDisplayed()

        //Game release date should be shown
        composeTestRule.onNodeWithText("Released").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Calendar Date").assertIsDisplayed()
        composeTestRule.onNodeWithText("2013-09-17").assertIsDisplayed()

        //Game rating should be shown
        composeTestRule.onNodeWithText("Rating").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Star Rating").assertIsDisplayed()
        composeTestRule.onNodeWithText("4.5").assertIsDisplayed()

        //Game description should be shown
        composeTestRule.onNodeWithText("About").assertIsDisplayed()
        composeTestRule.onNodeWithText("Show More").assertIsDisplayed()
        composeTestRule.onNodeWithText("Max Payne is a man with nothing to lose in the violent, cold urban night. A fugitive undercover cop framed for murder and now hunted by cops and the mob. Max is a man with his back against the wall, fighting a battle he cannot hope to win").assertIsDisplayed()

        //Game platforms should be shown
        composeTestRule.onNodeWithText("Platforms").assertIsDisplayed()
        composeTestRule.onNodeWithText("PlayStation 3").assertIsDisplayed()

        //Game stores should be shown
        composeTestRule.onNodeWithText("Stores").assertIsDisplayed()
        composeTestRule.onNodeWithText("PlayStation Store").assertIsDisplayed()

        composeTestRule.onNodeWithText("Developer").performScrollTo()
        composeTestRule.onNodeWithText("Remedy Entertainment, Rockstar Games").performScrollTo()

        //Game developers should be shown
        composeTestRule.onNodeWithText("Developer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Remedy Entertainment, Rockstar Games").assertIsDisplayed()

        composeTestRule.onNodeWithText("Publisher").performScrollTo()
        composeTestRule.onNodeWithText("Rockstar Games, Microsoft Studios").performScrollTo()

        //Game publishers should be shown
        composeTestRule.onNodeWithText("Publisher").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rockstar Games, Microsoft Studios").assertIsDisplayed()
    }

    @Test
    fun play_button_should_be_clickable() {
        every { gameDetailsViewModel.uiState() } answers { MutableStateFlow(GameDetailsState(ScreenState.Success, FakeGamesData.getFakeGameDetails(), null)) }
        var openGameTrailer = false
        composeTestRule.setContent {
            EpicWorldTheme {
                GameDetailsScreen(gameId = 123, navigateBack = { }, openGameTrailer = { openGameTrailer = true }, gameDetailsViewModel)
            }
        }
        composeTestRule.onNodeWithContentDescription("Play Trailer").assertIsDisplayed().assertHasClickAction().performClick()

        Assert.assertTrue(openGameTrailer)
    }

    @Test
    fun show_more_should_not_be_visible_for_short_description() {
        every { gameDetailsViewModel.uiState() } answers { MutableStateFlow(GameDetailsState(ScreenState.Success, FakeGamesData.getFakeGameDetailsShortDesc(), null)) }
        composeTestRule.setContent {
            EpicWorldTheme {
                GameDetailsScreen(gameId = 123, navigateBack = { }, openGameTrailer = { }, gameDetailsViewModel)
            }
        }
        composeTestRule.onNodeWithText("This is a cool shooting game!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Show More").assertDoesNotExist()
    }

    @Test
    fun show_more_should_be_clickable_and_show_less_should_be_shown_on_click() {
        every { gameDetailsViewModel.uiState() } answers { MutableStateFlow(GameDetailsState(ScreenState.Success, FakeGamesData.getFakeGameDetails(), null)) }
        composeTestRule.setContent {
            EpicWorldTheme {
                GameDetailsScreen(gameId = 123, navigateBack = { }, openGameTrailer = { }, gameDetailsViewModel)
            }
        }
        composeTestRule.onNodeWithText("Show Less").assertDoesNotExist()
        composeTestRule.onNodeWithText("Show More").assertIsDisplayed().assertHasClickAction().performClick()
        composeTestRule.onNodeWithText("Show Less").assertIsDisplayed()
    }

    @Test
    fun show_less_should_be_clickable_show_more_should_be_shown_on_click() {
        every { gameDetailsViewModel.uiState() } answers { MutableStateFlow(GameDetailsState(ScreenState.Success, FakeGamesData.getFakeGameDetails(), null)) }
        composeTestRule.setContent {
            EpicWorldTheme {
                GameDetailsScreen(gameId = 123, navigateBack = { }, openGameTrailer = { }, gameDetailsViewModel)
            }
        }
        composeTestRule.onNodeWithText("Show Less").assertDoesNotExist()
        val showMoreView = composeTestRule.onNodeWithText("Show More")
        showMoreView.assertIsDisplayed().assertHasClickAction().performClick()
        val showLessView = composeTestRule.onNodeWithText("Show Less")
        showLessView.assertIsDisplayed().assertHasClickAction()
        showMoreView.assertDoesNotExist()
        showLessView.performClick()
    }

}