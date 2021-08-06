package com.ruben.epicworld.presentation.details.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.commonui.LoadingView
import com.ruben.epicworld.presentation.details.GameDetailsViewModel
import com.ruben.epicworld.presentation.utility.showToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Created by Ruben Quadros on 06/08/21
 **/
@Composable
fun GameDetailsScreen(
    gameId: Int,
    gameScreenShots: List<String>,
    navigateBack: () -> Unit,
    gameDetailsViewModel: GameDetailsViewModel = hiltViewModel()
) {
    HandleSideEffect(gameDetailsViewModel.uiSideEffect())
    if (gameId == 0) {
        gameDetailsViewModel.handleGameIdError()
        navigateBack.invoke()
        return
    } else {
        gameDetailsViewModel.getGameDetails(gameId = gameId)
    }
    val state = gameDetailsViewModel.uiState().collectAsState()
    when (state.value.screenState) {
        is ScreenState.Loading -> {
           LoadingView(modifier = Modifier.fillMaxSize())
        }
        is ScreenState.Error -> {
            navigateBack.invoke()
        }
        is ScreenState.Success -> {
            state.value.gameDetails?.let {
                GameDetails(
                    gameScreenShots = gameScreenShots,
                    navigateBack = navigateBack,
                    gameDetails = it
                )
            }
        }
    }
}

@Composable
fun GameDetails(
    navigateBack: () -> Unit,
    gameScreenShots: List<String>,
    gameDetails: GameDetailsEntity
) {
    val hasGameScreenShots = gameScreenShots.isNotEmpty()
    Column {
        if (hasGameScreenShots) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
                painter = rememberImagePainter(
                    data = gameScreenShots[0],
                    builder = {
                        placeholder(R.drawable.app_logo)
                        crossfade(true)
                    }
                ),
                    contentDescription = stringResource(id = R.string.game_details_screenshots)
                )
        } else {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
                painter = rememberImagePainter(
                    data = gameDetails.backgroundImage,
                    builder = {
                        placeholder(R.drawable.app_logo)
                        crossfade(true)
                    }
                ),
                contentDescription = stringResource(id = R.string.game_details_screenshots)
            )
        }
    }
}

@Composable
fun HandleSideEffect(sideEffectFlow: Flow<GameDetailsSideEffect>) {
    val context = LocalContext.current
    val gameIdError = stringResource(id = R.string.game_details_invalid_game_id)
    val genericError = stringResource(id = R.string.all_generic_error)
    LaunchedEffect(sideEffectFlow) {
        sideEffectFlow.collect { uiSideEffect ->
            when (uiSideEffect) {
                is GameDetailsSideEffect.ShowGameDetailsErrorToast -> {
                    context.showToast(genericError)
                }
                is GameDetailsSideEffect.ShowGameIdErrorToast -> {
                    context.showToast(gameIdError)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameDetailsPreview() {
    GameDetails(
        navigateBack = {  },
        gameScreenShots = arrayListOf(),
        gameDetails = GameDetailsEntity(1, "Max Payne", "", 4.5, "",
            "", arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf())
    )
}