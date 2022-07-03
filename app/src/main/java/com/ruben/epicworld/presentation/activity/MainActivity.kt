package com.ruben.epicworld.presentation.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ruben.epicworld.presentation.EpicWorldApp
import com.ruben.epicworld.presentation.base.BaseActivity
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            EpicWorldTheme {
                EpicWorldApp()
            }
        }
    }
}