package com.ruben.epicworld.presentation.commonui

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Ruben Quadros on 05/08/21
 **/
class SnackbarView(private val scope: CoroutineScope) {

    private var snackBarJob: Job? = null

    fun showSnackBar(snackbarHostState: SnackbarHostState, message: String) {
        snackBarJob?.cancel()
        snackBarJob = scope.launch {
            snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
        }
    }

    fun showSnackBarWithNoAction(snackbarHostState: SnackbarHostState, message: String, actionLabel: String) {
        snackBarJob?.cancel()
        snackBarJob = scope.launch {
            snackbarHostState.showSnackbar(message = message, actionLabel = actionLabel, duration = SnackbarDuration.Indefinite)
        }
    }

    fun showSnackBarWithAction(snackbarHostState: SnackbarHostState, message: String, actionLabel: String) {
        snackBarJob?.cancel()
        snackBarJob = scope.launch {
            when (snackbarHostState.showSnackbar(message = message, actionLabel = actionLabel, duration = SnackbarDuration.Indefinite)) {
                SnackbarResult.ActionPerformed -> {
                    //action clicked
                }
                SnackbarResult.Dismissed -> {
                    //Snack bar has been dismissed by user of another snackbar
                }
            }
        }
    }
}