package com.ruben.epicworld.compose

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.ruben.epicworld.presentation.search.GameSearchScreen
import com.ruben.epicworld.presentation.search.GameSearchViewModel
import com.ruben.epicworld.presentation.search.SearchState
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Ruben Quadros on 18/09/21
 **/
class GameSearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val gameSearchViewModel = mockk<GameSearchViewModel>()

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
        every { gameSearchViewModel.initData() } answers { }
        every { gameSearchViewModel.createInitialState() } answers {
            SearchState.InitialState
        }
        every { gameSearchViewModel.uiState() } answers { MutableStateFlow(gameSearchViewModel.createInitialState()) }
        every { gameSearchViewModel.uiSideEffect() } answers  { flow {  } }
        every { gameSearchViewModel.handleDetailsNavigation(any()) } answers { }
    }

    @Test
    fun search_bar_should_be_shown() {
        composeTestRule.setContent {
            EpicWorldTheme {
                GameSearchScreen(navigateBack = { }, navigateToDetails = { }, gameSearchViewModel = gameSearchViewModel)
            }
        }

        composeTestRule.onNodeWithTag("Search Bar").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Back Button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Bottom Border").assertIsDisplayed()
    }

    @Test
    fun clear_search_button_should_be_shown_on_entering_text_in_search_bar() {
        every { gameSearchViewModel.searchGame(any()) } answers {  }
        composeTestRule.setContent {
            EpicWorldTheme {
                GameSearchScreen(navigateBack = { }, navigateToDetails = { }, gameSearchViewModel = gameSearchViewModel)
            }
        }

        composeTestRule.onNodeWithTag("Search Bar").performTextInput("gta")
        composeTestRule.onNodeWithTag("Search Bar").assert(hasText("gta"))
        composeTestRule.onNodeWithContentDescription("Back Button").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Clear Button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Bottom Border").assertIsDisplayed()

    }

    @Test
    fun search_bar_back_button_and_clear_button_should_be_clickable() {
        every { gameSearchViewModel.searchGame(any()) } answers {  }
        composeTestRule.setContent {
            EpicWorldTheme {
                GameSearchScreen(navigateBack = { }, navigateToDetails = { }, gameSearchViewModel = gameSearchViewModel)
            }
        }

        composeTestRule.onNodeWithTag("Search Bar").performTextInput("gta")
        composeTestRule.onNodeWithContentDescription("Back Button").assertHasClickAction()
        composeTestRule.onNodeWithContentDescription("Clear Button").assertHasClickAction()
    }

    @Test
    fun should_show_loader_on_entering_query_in_search_bar() {
        every { gameSearchViewModel.searchGame(any()) } answers {  }
        every { gameSearchViewModel.uiState() } answers { MutableStateFlow(SearchState.LoadingState) }
        composeTestRule.setContent {
            EpicWorldTheme {
                GameSearchScreen(navigateBack = { }, navigateToDetails = { }, gameSearchViewModel = gameSearchViewModel)
            }
        }

        composeTestRule.onNodeWithTag("ProgressBar").assertIsDisplayed()
    }

    @Test
    fun should_show_search_results_once_results_are_fetcher() {
        every { gameSearchViewModel.searchGame(any()) } answers {  }
        every { gameSearchViewModel.uiState() } answers {
            MutableStateFlow(
                SearchState.SearchResultState(
                    FakeGamesData.getFakeSearchResults()
                )
            )
        }
        composeTestRule.setContent {
            EpicWorldTheme {
                GameSearchScreen(navigateBack = { }, navigateToDetails = { }, gameSearchViewModel = gameSearchViewModel)
            }
        }

        composeTestRule.onAllNodesWithContentDescription("Game Image").assertCountEquals(6)
        composeTestRule.onNodeWithText("Grand Theft Auto").assertIsDisplayed()
        composeTestRule.onNodeWithText("GTA-bankrob").assertIsDisplayed()
        composeTestRule.onNodeWithText("GTA - Bombay").assertIsDisplayed()
        composeTestRule.onNodeWithText("GTA-V-DEMO").assertIsDisplayed()
        composeTestRule.onNodeWithText("gta multiplayer").assertIsDisplayed()
        composeTestRule.onNodeWithText("GTA Prototype").assertIsDisplayed()
    }

    @Test
    fun search_results_should_be_clickable() {
        every { gameSearchViewModel.searchGame(any()) } answers {  }
        every { gameSearchViewModel.uiState() } answers {
            MutableStateFlow(
                SearchState.SearchResultState(
                    FakeGamesData.getFakeSearchResults()
                )
            )
        }
        composeTestRule.setContent {
            EpicWorldTheme {
                GameSearchScreen(navigateBack = { }, navigateToDetails = { }, gameSearchViewModel = gameSearchViewModel)
            }
        }

        composeTestRule.onAllNodesWithTag("Search Result Parent").assertCountEquals(6).onFirst().assertHasClickAction()
    }

    @Test
    fun should_show_no_results_ui_when_no_games_match_search_query() {
        every { gameSearchViewModel.searchGame(any()) } answers {  }
        every { gameSearchViewModel.uiState() } answers { MutableStateFlow(SearchState.NoResultsState) }
        composeTestRule.setContent {
            EpicWorldTheme {
                GameSearchScreen(navigateBack = { }, navigateToDetails = { }, gameSearchViewModel = gameSearchViewModel)
            }
        }

        composeTestRule.onNodeWithText("No matching results").assertIsDisplayed()
    }

}