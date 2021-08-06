package com.ruben.epicworld.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import com.ruben.epicworld.presentation.commonui.LoadingItem
import com.ruben.epicworld.presentation.commonui.LoadingView
import org.junit.Rule
import org.junit.Test

/**
 * Created by Ruben Quadros on 02/08/21
 **/
@ExperimentalFoundationApi
class LoaderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun should_be_able_to_display_progress_bar_item() {
        composeTestRule.setContent {
            LoadingItem()
        }
        composeTestRule.onNode(hasTestTag("ProgressBarItem")).assertIsDisplayed()
    }

    @Test
    fun should_be_able_to_display_progress_bar() {
        composeTestRule.setContent {
            LoadingView()
        }
        composeTestRule.onNode(hasTestTag("ProgressBar")).assertIsDisplayed()
    }
}