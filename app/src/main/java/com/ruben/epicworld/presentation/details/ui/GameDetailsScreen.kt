package com.ruben.epicworld.presentation.details.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.commonui.BottomRoundedArcShape
import com.ruben.epicworld.presentation.commonui.LoadingView
import com.ruben.epicworld.presentation.details.GameDetailsViewModel
import com.ruben.epicworld.presentation.theme.AmberA400
import com.ruben.epicworld.presentation.theme.Black
import com.ruben.epicworld.presentation.theme.PinkA400
import com.ruben.epicworld.presentation.theme.Typography
import com.ruben.epicworld.presentation.utility.ApplicationUtility
import com.ruben.epicworld.presentation.utility.showToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Created by Ruben Quadros on 06/08/21
 **/
@Composable
fun GameDetailsScreen(
    gameId: Int,
    navigateBack: () -> Unit,
    openGameTrailer: () -> Unit,
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
                    gameDetails = it,
                    openGameTrailer = openGameTrailer
                )
            }
        }
    }
}

@Composable
fun GameDetails(
    gameDetails: GameDetailsEntity,
    openGameTrailer: () -> Unit
) {
    val scrollState = rememberScrollState()
    val shouldShowMore = remember {
        mutableStateOf(DescriptionStatus.DEFAULT)
    }
    val maxLines = remember {
        mutableStateOf(4)
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(scrollState)
        .testTag("GameDetailsParent")) {
        ConstraintLayout {
            val (play, gameImage) = createRefs()
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .graphicsLayer {
                        clip = true
                        shape = BottomRoundedArcShape()
                    }
                    .constrainAs(gameImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
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
            PlayTrailer(
                openGameTrailer = openGameTrailer,
                modifier = Modifier.constrainAs(play) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 30.dp, end = 16.dp),
            text = gameDetails.name,
            style = Typography.h3,
            color = Black
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = ApplicationUtility.getGenres(gameDetails.genresEntity),
            style = Typography.body2,
            color = Black
        )
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (dateLabel, dateValue, ratingLabel, ratingValue) = createRefs()
            Row(modifier = Modifier
                .wrapContentSize()
                .constrainAs(dateLabel) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }) {
                Text(
                    text = stringResource(id = R.string.game_details_released),
                    style = Typography.h6,
                    color = Black
                )
                Icon(
                    modifier = Modifier.padding(start = 8.dp),
                    imageVector = Icons.Filled.Update,
                    contentDescription = stringResource(id = R.string.game_details_calendar_date),
                    tint = AmberA400
                )
            }
            Text(
                modifier = Modifier.constrainAs(dateValue) {
                    top.linkTo(dateLabel.bottom, margin = 8.dp)
                    start.linkTo(dateLabel.start)
                    end.linkTo(dateLabel.end)
                },
                text = gameDetails.released,
                textAlign = TextAlign.Center,
                color = Black,
                style = Typography.body2,
            )
            Row(modifier = Modifier
                .wrapContentSize()
                .constrainAs(ratingLabel) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }) {
                Text(
                    text = stringResource(id = R.string.game_details_rating),
                    style = Typography.h6,
                    color = Black
                )
                Icon(
                    modifier = Modifier.padding(start = 8.dp),
                    imageVector = Icons.Filled.StarRate,
                    contentDescription = stringResource(id = R.string.all_star_rating),
                    tint = AmberA400
                )
            }
            Text(
                modifier = Modifier.constrainAs(ratingValue) {
                    top.linkTo(ratingLabel.bottom, margin = 8.dp)
                    start.linkTo(ratingLabel.start)
                    end.linkTo(ratingLabel.end)
                },
                text = gameDetails.rating.toString(),
                textAlign = TextAlign.Center,
                color = Black,
                style = Typography.body2,
            )
        }
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(id = R.string.game_details_about),
            style = Typography.h6,
            color = Black
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = gameDetails.description,
            style = Typography.body2,
            color = Black,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines.value,
            onTextLayout = {
                if (it.lineCount == 4 && it.isLineEllipsized(3)) {
                    shouldShowMore.value = DescriptionStatus.SHOW_MORE
                } else if(it.lineCount > 4) {
                    shouldShowMore.value = DescriptionStatus.SHOW_LESS
                }
            }
        )
        when (shouldShowMore.value) {
            DescriptionStatus.SHOW_MORE -> {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .clickable {
                            maxLines.value = Int.MAX_VALUE
                        },
                    text = stringResource(id = R.string.game_details_about_show_more),
                    style = Typography.body2,
                    textDecoration = TextDecoration.Underline,
                    color = PinkA400
                )
            }
            DescriptionStatus.SHOW_LESS -> {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .clickable {
                            maxLines.value = 4
                        },
                    text = stringResource(id = R.string.game_details_about_show_less),
                    style = Typography.body2,
                    textDecoration = TextDecoration.Underline,
                    color = PinkA400
                )
            }
            else -> {
                //do nothing
            }
        }
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = stringResource(id = R.string.game_details_platforms),
            style = Typography.h6,
            color = Black
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = ApplicationUtility.getPlatforms(gameDetails.platformsEntity),
            style = Typography.body2,
            color = Black
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = stringResource(id = R.string.game_details_stores),
            style = Typography.h6,
            color = Black
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = ApplicationUtility.getStores(gameDetails.storesEntity),
            style = Typography.body2,
            color = Black
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = stringResource(id = R.string.game_details_developer),
            style = Typography.h6,
            color = Black
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = ApplicationUtility.getDevelopers(gameDetails.developersEntity),
            style = Typography.body2,
            color = Black
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = stringResource(id = R.string.game_details_publisher),
            style = Typography.h6,
            color = Black
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
            text = ApplicationUtility.getPublishers(gameDetails.publishersEntity),
            style = Typography.body2,
            color = Black
        )
    }
}

@Composable
fun PlayTrailer(modifier: Modifier = Modifier, openGameTrailer: () -> Unit) {
    Box(modifier = modifier
        .offset(0.dp, 25.dp)) {
        IconButton(onClick = openGameTrailer) {
            Image(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        shadowElevation = 20.dp.toPx()
                        shape = RoundedCornerShape(15.dp)
                        clip = true
                    }
                    .background(PinkA400),
                painter = painterResource(id =R.drawable.ic_play),
                contentDescription = stringResource(id = R.string.game_details_play_trailer)
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
        gameDetails = GameDetailsEntity(1, "Max Payne", "The third game in a series, it holds nothing back from the player. Open world adventures of the renowned monster slayer Geralt of Rivia are now even on a larger scale. Following the source material more accurately, this time Geralt is trying to find the child of the prophecy, Ciri while making a quick coin from various contracts on the side", 4.5, "",
            "", arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf()),
        openGameTrailer = {  }
    )
}