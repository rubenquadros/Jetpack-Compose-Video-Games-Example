package com.ruben.epicworld.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.ruben.epicworld.presentation.home.ui.HomeScreen
import com.ruben.epicworld.presentation.home.ui.HomeState
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Ruben Quadros on 02/08/21
 **/
@ExperimentalFoundationApi
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun app_bar_should_be_displayed_in_home_screen() {
        val uiState = MutableStateFlow(HomeState.InitialState)
        composeTestRule.setContent {
            EpicWorldTheme {
                HomeScreen(
                    uiState = uiState,
                    searchClick = {  },
                    filterClick = {  },
                    gameClick = {  }
                )
            }
        }
        composeTestRule.onNodeWithText("Epic World").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Filter Results").assertIsDisplayed()
    }

    @Test
    fun games_should_be_displayed_in_hone_screen() {
        val uiState = MutableStateFlow(HomeState.AllGamesData(FakeGamesData.getFakePagingData()))
        composeTestRule.setContent {
            EpicWorldTheme {
                HomeScreen(
                    uiState = uiState,
                    searchClick = {  },
                    filterClick = {  },
                    gameClick = {  }
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
    fun games_should_be_clickable() {
        val uiState = MutableStateFlow(HomeState.AllGamesData(FakeGamesData.getFakePagingData()))
        var isGameClicked = false
        composeTestRule.setContent {
            EpicWorldTheme {
                HomeScreen(
                    uiState = uiState,
                    searchClick = {  },
                    filterClick = {  },
                    gameClick = { isGameClicked = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Max Payne").performClick()
        Assert.assertTrue(isGameClicked)
    }
}