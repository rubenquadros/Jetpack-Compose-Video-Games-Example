package com.ruben.epicworld.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.ruben.epicworld.domain.entity.base.ErrorRecord
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.home.HomeViewModel
import com.ruben.epicworld.presentation.home.ui.HomeScreen
import com.ruben.epicworld.presentation.home.ui.HomeSideEffect
import com.ruben.epicworld.presentation.home.ui.HomeState
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
 * Created by Ruben Quadros on 02/08/21
 **/
@ExperimentalFoundationApi
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val homeViewModel = mockk<HomeViewModel>()

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
        every { homeViewModel.initData() } answers {  }
        every { homeViewModel.uiSideEffect() } answers  { flow {  } }
        every { homeViewModel.uiState() } answers { MutableStateFlow(HomeState(ScreenState.Success, FakeGamesData.getFakePagingData(), null)) }
    }

    @Test
    fun app_bar_should_be_displayed_in_home_screen() {
        composeTestRule.setContent {
            EpicWorldTheme {
                HomeScreen(
                    openSearch = { },
                    openFilters = { },
                    openGameDetails = { },
                    homeViewModel = homeViewModel
                )
            }
        }
        composeTestRule.onNodeWithText("Epic World").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Filter Results").assertIsDisplayed()
    }

    @Test
    fun games_should_be_displayed_in_home_screen() {
        composeTestRule.setContent {
            EpicWorldTheme {
                HomeScreen(
                    openSearch = { },
                    openFilters = { },
                    openGameDetails = { },
                    homeViewModel = homeViewModel
                )
            }
        }
        composeTestRule.onNodeWithText("Max Payne").assertIsDisplayed()
        composeTestRule.onNodeWithText("4.5").assertIsDisplayed()
        composeTestRule.onAllNodesWithContentDescription("Game Image").assertCountEquals(2)
        composeTestRule.onNodeWithText("GTA V").assertIsDisplayed()
        composeTestRule.onNodeWithText("4.8").assertIsDisplayed()
    }

    @Test
    fun should_show_error_ui_incase_of_error_state() {
        every { homeViewModel.uiState() } answers { MutableStateFlow(HomeState(ScreenState.Error, null, ErrorRecord.GenericError)) }
        composeTestRule.setContent {
            EpicWorldTheme {
                HomeScreen(
                    openSearch = { },
                    openFilters = { },
                    openGameDetails = { },
                    homeViewModel = homeViewModel
                )
            }
        }
        composeTestRule.onNodeWithText("There was an error. We are terribly sorry!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun games_should_be_clickable() {
        var isGameClicked = false
        composeTestRule.setContent {
            EpicWorldTheme {
                HomeScreen(
                    openSearch = { },
                    openFilters = { },
                    openGameDetails = { isGameClicked = true },
                    homeViewModel = homeViewModel
                )
            }
        }
        composeTestRule.onNodeWithText("Max Payne").performClick()
        Assert.assertTrue(isGameClicked)
    }

    @Test
    fun snack_bar_should_be_shown_when_ui_side_effect_is_posted() {
        every { homeViewModel.uiSideEffect() } answers  {
            flow { emit(HomeSideEffect.ShowSnackBar("Error", "OK")) }
        }
        composeTestRule.setContent {
            EpicWorldTheme {
                HomeScreen(
                    openSearch = { },
                    openFilters = { },
                    openGameDetails = {  },
                    homeViewModel = homeViewModel
                )
            }
        }
        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
    }
}