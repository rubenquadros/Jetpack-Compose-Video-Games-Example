package com.ruben.epicworld.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import com.ruben.epicworld.presentation.base.BaseActivity

/**
 * Created by Ruben Quadros on 31/07/21
 **/
@ExperimentalAnimationApi
@SuppressLint("CustomSplashScreen")
@ExperimentalFoundationApi
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}