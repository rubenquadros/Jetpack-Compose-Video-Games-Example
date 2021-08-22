package com.ruben.epicworld.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import org.junit.*

/**
 * Created by Ruben Quadros on 02/08/21
 **/
@ExperimentalFoundationApi
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val homeViewModel = mockk<HomeViewModel>()

    private val composeIdlingResource = ComposeIdlingResource()

    @Before
    fun init() {
        composeTestRule.registerIdlingResource(composeIdlingResource)
        MockKAnnotations.init(this, true)
        every { homeViewModel.initData() } answers {
            composeIdlingResource.isAppIdle(false)
        }
        every { homeViewModel.createInitialState() } answers {
            HomeState(ScreenState.Loading, null, null)
        }
        every { homeViewModel.uiSideEffect() } answers  {
            composeIdlingResource.isAppIdle(true)
            flow {  }
        }
    }

    @After
    fun tearDown() {
        composeTestRule.unregisterIdlingResource(composeIdlingResource)
    }

    @Test
    fun app_bar_should_be_displayed_in_home_screen() {
        val fakeData = FakeGamesData.getFakePagingData().flowOn(Dispatchers.Unconfined)
            .onCompletion { composeIdlingResource.isAppIdle(true) }
        every { homeViewModel.uiState() } answers {
            MutableStateFlow(HomeState(ScreenState.Success, fakeData, null))
        }
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
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Epic World").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Filter Results").assertIsDisplayed()

    }

    @Test
    fun games_should_be_displayed_in_home_screen() {
        val fakeData = FakeGamesData.getFakePagingData().flowOn(Dispatchers.Unconfined)
            .onCompletion { composeIdlingResource.isAppIdle(true) }
        every { homeViewModel.uiState() } answers {
            MutableStateFlow(HomeState(ScreenState.Success, fakeData, null))
        }
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
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Max Payne").assertIsDisplayed()
        composeTestRule.onNodeWithText("4.5").assertIsDisplayed()
        composeTestRule.onAllNodesWithContentDescription("Game Image").assertCountEquals(6)
        composeTestRule.onAllNodesWithContentDescription("Star Rating").assertCountEquals(6)
        composeTestRule.onNodeWithText("GTA V").assertIsDisplayed()
        composeTestRule.onNodeWithText("4.8").assertIsDisplayed()
    }

    @Test
    fun games_should_be_scrollable() {
        val fakeData = FakeGamesData.getFakePagingData().flowOn(Dispatchers.Unconfined)
            .onCompletion { composeIdlingResource.isAppIdle(true) }
        every { homeViewModel.uiState() } answers {
            MutableStateFlow(HomeState(ScreenState.Success, fakeData, null))
        }
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
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Limbo").performScrollTo()
        composeTestRule.onNodeWithText("Limbo").assertIsDisplayed()
        composeTestRule.onNodeWithText("4.2").assertIsDisplayed()
    }

    @Test
    fun should_show_error_ui_incase_of_error_state() {
        every { homeViewModel.uiState() } answers {
            composeIdlingResource.isAppIdle(true)
            MutableStateFlow(HomeState(ScreenState.Error, null, ErrorRecord.GenericError))
        }
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
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("There was an error. We are terribly sorry!")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun games_should_be_clickable() {
        var isGameClicked = false
        val fakeData = FakeGamesData.getFakePagingData().flowOn(Dispatchers.Unconfined)
            .onCompletion { composeIdlingResource.isAppIdle(true) }
        every { homeViewModel.uiState() } answers {
            MutableStateFlow(HomeState(ScreenState.Success, fakeData, null))
        }
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
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Max Payne").performClick()
        Assert.assertTrue(isGameClicked)
    }

    @Test
    fun snack_bar_should_be_shown_when_ui_side_effect_is_posted() {
        every { homeViewModel.uiState() } answers {
            MutableStateFlow(homeViewModel.createInitialState())
        }
        every { homeViewModel.uiSideEffect() } answers {
            flow { emit(HomeSideEffect.ShowSnackBar("Error", "OK")) }.flowOn(Dispatchers.Unconfined)
                .onCompletion { composeIdlingResource.isAppIdle(true) }
        }
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
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
    }
}