package com.ruben.epicworld.presentation.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import com.ruben.epicworld.presentation.EpicWorldApp
import com.ruben.epicworld.presentation.base.BaseActivity
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EpicWorldTheme {
                EpicWorldApp()
            }
        }
    }
}