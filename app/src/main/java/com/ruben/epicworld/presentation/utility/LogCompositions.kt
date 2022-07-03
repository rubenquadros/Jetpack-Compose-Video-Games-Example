package com.ruben.epicworld.presentation.utility

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.ruben.epicworld.BuildConfig
import com.ruben.epicworld.presentation.utility.Constants.TAG

/**
 * Created by Ruben Quadros on 25/06/22
 **/
class Ref(var value: Int)

// Note the inline function below which ensures that this function is essentially
// copied at the call site to ensure that its logging only recompositions from the
// original call site.
@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun LogCompositions(tag: String) {
    if (BuildConfig.DEBUG) {
        val ref = remember { Ref(0) }
        SideEffect { ref.value++ }
        Log.d(TAG, "Compositions: $tag: ${ref.value}")
    }
}