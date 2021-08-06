package com.ruben.epicworld.presentation.utility

import android.content.Context
import android.widget.Toast

/**
 * Created by Ruben Quadros on 06/08/21
 **/
fun Context.showToast(message: String)  {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}