package com.ruben.epicworld.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import com.ruben.epicworld.presentation.base.BaseActivity
import com.ruben.epicworld.presentation.home.HomeActivity

/**
 * Created by Ruben Quadros on 31/07/21
 **/
@SuppressLint("CustomSplashScreen")
@ExperimentalFoundationApi
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}