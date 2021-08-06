package com.ruben.epicworld.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.ruben.epicworld.presentation.commonui.HomeAppBar
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Ruben Quadros on 02/08/21
 **/
@ExperimentalFoundationApi
class AppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun given_title_should_be_displayed_in_home_app_bar() {
        composeTestRule.setContent {
            EpicWorldTheme {
                HomeAppBar(title = "Title", searchClick = {  }, filterClick = {  })
            }
        }
        composeTestRule.onNodeWithText("Title").assertIsDisplayed()
    }

    @Test
    fun app_bar_icons_should_be_displayed_in_home_app_bar() {
        composeTestRule.setContent {
            EpicWorldTheme {
                HomeAppBar(title = "Title", searchClick = {  }, filterClick = {  })
            }
        }
        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Filter Results").assertIsDisplayed()
    }

    @Test
    fun app_bar_icons_should_be_clickable_in_home_app_bar() {
        var testSearchClick = false
        var testFilterClick = false
        composeTestRule.setContent {
            EpicWorldTheme {
                HomeAppBar(title = "Title", searchClick = { testSearchClick = true }, filterClick = { testFilterClick = true })
            }
        }
        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.onNodeWithContentDescription("Filter Results").performClick()
        Assert.assertTrue(testSearchClick)
        Assert.assertTrue(testFilterClick)
    }
}