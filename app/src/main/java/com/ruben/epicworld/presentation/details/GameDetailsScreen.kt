package com.ruben.epicworld.presentation.details

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
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.commonui.BottomRoundedArcShape
import com.ruben.epicworld.presentation.commonui.LoadingView
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import com.ruben.epicworld.presentation.utility.Constants.DESCRIPTION_LINES
import com.ruben.epicworld.presentation.utility.LogCompositions
import com.ruben.epicworld.presentation.utility.setPortrait
import com.ruben.epicworld.presentation.utility.showToast

/**
 * Created by Ruben Quadros on 06/08/21
 **/
@Composable
fun GameDetailsScreen(
    navigateBack: () -> Unit,
    openGameTrailer: (Int) -> Unit,
    gameDetailsViewModel: GameDetailsViewModel = hiltViewModel()
) {

    LogCompositions(tag = "GameDetailsScreen")

    val context = LocalContext.current
    val state by gameDetailsViewModel.uiState().collectAsStateWithLifecycle()

    val gameIdError = stringResource(id = R.string.game_details_invalid_game_id)
    val genericError = stringResource(id = R.string.all_generic_error)

    LaunchedEffect(key1 = true) {
        context.setPortrait()
    }

    LaunchedEffect(gameDetailsViewModel.uiSideEffect()) {
        gameDetailsViewModel.uiSideEffect().collect { uiSideEffect ->
            when (uiSideEffect) {
                is GameDetailsSideEffect.ShowGameDetailsErrorToast -> {
                    context.showToast(genericError)
                }
                is GameDetailsSideEffect.ShowGameIdErrorToast -> {
                    context.showToast(gameIdError)
                    navigateBack.invoke()
                }
            }
        }
    }

    when (state.screenState) {
        is ScreenState.Loading -> {
           LoadingView(modifier = Modifier
               .fillMaxSize()
               .systemBarsPadding())
        }
        is ScreenState.Error -> {
            navigateBack.invoke()
        }
        is ScreenState.Success -> {
            state.gameDetails?.let {
                GameDetails(
                    modifier = Modifier.systemBarsPadding(),
                    gameDetails = it,
                    openGameTrailer = openGameTrailer
                )
            }
        }
    }
}

@Composable
private fun GameDetails(
    modifier: Modifier = Modifier,
    gameDetails: GameDetailsEntity,
    openGameTrailer: (Int) -> Unit
) {
    LogCompositions(tag = "GameDetails")

    val scrollState = rememberScrollState()

    Column(modifier = modifier
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
                        shadowElevation = 50.dp.toPx()
                    }
                    .constrainAs(gameImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                contentScale = ContentScale.Crop,
                painter = rememberAsyncImagePainter(
                    model = gameDetails.backgroundImage,
                    placeholder = painterResource(id = R.drawable.app_logo)
                ),
                contentDescription = stringResource(id = R.string.game_details_screenshots)
            )
            if (gameDetails.moviesCount > 0) {
                PlayTrailer(
                    openGameTrailer = { openGameTrailer(gameDetails.id) },
                    modifier = Modifier.constrainAs(play) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
            }
        }
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 30.dp, end = 16.dp),
            text = gameDetails.name,
            style = EpicWorldTheme.typography.title3,
            color = EpicWorldTheme.colors.background
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = gameDetails.genresEntity.joinToString(separator = " ") { it.name },
            style = EpicWorldTheme.typography.body2,
            color = EpicWorldTheme.colors.background
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
                    style = EpicWorldTheme.typography.subTitle2,
                    color = EpicWorldTheme.colors.background
                )
                Icon(
                    modifier = Modifier.padding(start = 8.dp),
                    imageVector = Icons.Filled.Update,
                    contentDescription = stringResource(id = R.string.game_details_calendar_date),
                    tint = EpicWorldTheme.colors.secondary
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
                color = EpicWorldTheme.colors.background,
                style = EpicWorldTheme.typography.body2,
            )
            Row(modifier = Modifier
                .wrapContentSize()
                .constrainAs(ratingLabel) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }) {
                Text(
                    text = stringResource(id = R.string.game_details_rating),
                    style = EpicWorldTheme.typography.subTitle2,
                    color = EpicWorldTheme.colors.background
                )
                Icon(
                    modifier = Modifier.padding(start = 8.dp),
                    imageVector = Icons.Filled.StarRate,
                    contentDescription = stringResource(id = R.string.all_star_rating),
                    tint = EpicWorldTheme.colors.secondary
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
                color = EpicWorldTheme.colors.background,
                style = EpicWorldTheme.typography.body2,
            )
        }
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(id = R.string.game_details_about),
            style = EpicWorldTheme.typography.subTitle2,
            color = EpicWorldTheme.colors.background
        )
        ExpandableDescription(description = gameDetails.description)
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = stringResource(id = R.string.game_details_platforms),
            style = EpicWorldTheme.typography.subTitle2,
            color = EpicWorldTheme.colors.background
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = gameDetails.platformsEntity.joinToString { it.platform.name },
            style = EpicWorldTheme.typography.body2,
            color = EpicWorldTheme.colors.background
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = stringResource(id = R.string.game_details_stores),
            style = EpicWorldTheme.typography.subTitle2,
            color = EpicWorldTheme.colors.background
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = gameDetails.storesEntity.joinToString { it.store.name },
            style = EpicWorldTheme.typography.body2,
            color = EpicWorldTheme.colors.background
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = stringResource(id = R.string.game_details_developer),
            style = EpicWorldTheme.typography.subTitle2,
            color = EpicWorldTheme.colors.background
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = gameDetails.developersEntity.joinToString { it.name },
            style = EpicWorldTheme.typography.body2,
            color = EpicWorldTheme.colors.background
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = stringResource(id = R.string.game_details_publisher),
            style = EpicWorldTheme.typography.subTitle2,
            color = EpicWorldTheme.colors.background
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
            text = gameDetails.publishersEntity.joinToString { it.name },
            style = EpicWorldTheme.typography.body2,
            color = EpicWorldTheme.colors.background
        )
    }
}

@Composable
private fun ExpandableDescription(modifier: Modifier = Modifier, description: String) {
    LogCompositions(tag = "ExpandableDescription")

    var shouldShowMore by remember {
        mutableStateOf(DescriptionStatus.DEFAULT)
    }
    var maxLines by remember {
        mutableStateOf(4)
    }

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            text = description,
            style = EpicWorldTheme.typography.body2,
            color = EpicWorldTheme.colors.background,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines,
            onTextLayout = {
                if (it.lineCount == DESCRIPTION_LINES && it.isLineEllipsized(DESCRIPTION_LINES-1)) {
                    shouldShowMore = DescriptionStatus.SHOW_MORE
                } else if(it.lineCount > DESCRIPTION_LINES) {
                    shouldShowMore = DescriptionStatus.SHOW_LESS
                }
            }
        )
        when (shouldShowMore) {
            DescriptionStatus.SHOW_MORE -> {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable {
                            maxLines = Int.MAX_VALUE
                        },
                    text = stringResource(id = R.string.game_details_about_show_more),
                    style = EpicWorldTheme.typography.body2,
                    textDecoration = TextDecoration.Underline,
                    color = EpicWorldTheme.colors.primary
                )
            }
            DescriptionStatus.SHOW_LESS -> {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable {
                            maxLines = DESCRIPTION_LINES
                        },
                    text = stringResource(id = R.string.game_details_about_show_less),
                    style = EpicWorldTheme.typography.body2,
                    textDecoration = TextDecoration.Underline,
                    color = EpicWorldTheme.colors.primary
                )
            }
            else -> {
                //do nothing
            }
        }
    }
}

@Composable
private fun PlayTrailer(modifier: Modifier = Modifier, openGameTrailer: () -> Unit) {
    LogCompositions(tag = "PlayTrailer")

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
                    .background(EpicWorldTheme.colors.primary),
                painter = painterResource(id =R.drawable.ic_play),
                contentDescription = stringResource(id = R.string.game_details_play_trailer)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameDetailsPreview() {
    GameDetails(
        gameDetails = GameDetailsEntity(1, "Max Payne", "The third game in a series, it holds nothing back from the player. Open world adventures of the renowned monster slayer Geralt of Rivia are now even on a larger scale. Following the source material more accurately, this time Geralt is trying to find the child of the prophecy, Ciri while making a quick coin from various contracts on the side", 4.5, "",
            "", 8, emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList()),
        openGameTrailer = {  }
    )
}