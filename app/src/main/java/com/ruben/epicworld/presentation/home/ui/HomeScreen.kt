package com.ruben.epicworld.presentation.home.ui

import android.content.pm.ActivityInfo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.games.GameResultEntity
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.commonui.*
import com.ruben.epicworld.presentation.home.HomeViewModel
import com.ruben.epicworld.presentation.theme.EpicWorldTheme

/**
 * Created by Ruben Quadros on 01/08/21
 **/

@ExperimentalFoundationApi
@Composable
fun HomeScreen(
    openSearch: () -> Unit,
    openFilters: () -> Unit,
    openGameDetails: (Int) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(homeViewModel.uiSideEffect()) {
        val messageHost = SnackbarView(this)
        homeViewModel.uiSideEffect().collect { uiSideEffect ->
            when (uiSideEffect) {
                is HomeSideEffect.ShowSnackBar -> {
                    messageHost.showSnackBar(
                        snackbarHostState = scaffoldState.snackbarHostState,
                        message = uiSideEffect.message
                    )
                }
            }
        }
    }

    ScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    Scaffold(topBar = {
        HomeAppBar(
            title = stringResource(id = R.string.home_app_bar_title),
            searchClick = { openSearch.invoke() },
            filterClick = {  }
        )
    },
        scaffoldState = scaffoldState,
        content = { paddingValues ->  GameListing(paddingValues = paddingValues, openGameDetails = openGameDetails, homeViewModel = homeViewModel) }
    )
}

@ExperimentalFoundationApi
@Composable
fun GameListing(paddingValues: PaddingValues, openGameDetails: (Int) -> Unit, homeViewModel: HomeViewModel) {
    val errorMessage: String = stringResource(id = R.string.home_screen_scroll_error)
    val action: String = stringResource(id = R.string.all_ok)
    val lifecycleOwner = LocalLifecycleOwner.current
    val stateFlow = homeViewModel.uiState()
    val stateLifecycleAware = remember(lifecycleOwner, stateFlow) {
        stateFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val state by stateLifecycleAware.collectAsState(initial = homeViewModel.createInitialState())

    when (state.screenState) {
        is ScreenState.Loading -> {
            //do nothing
        }
        is ScreenState.Error -> {
            GetGamesError { homeViewModel.initData() }
        }
        is ScreenState.Success -> {
            val lazyGameItems = state.games?.collectAsLazyPagingItems()
            lazyGameItems?.let { gameItems ->
                LazyVerticalGrid(modifier = Modifier.padding(paddingValues), columns = GridCells.Fixed(count = 2), content = {
                    items(gameItems.itemCount) { index ->
                        gameItems[index]?.let {
                            GameItem(game = it, gameClick = openGameDetails)
                        }
                    }

                    gameItems.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item { LoadingItem() }
                                item { LoadingItem() }
                            }
                            loadState.append is LoadState.Loading -> {
                                item { LoadingItem() }
                                item { LoadingItem() }
                            }
                            loadState.refresh is LoadState.Error -> {
                                homeViewModel.handlePaginationDataError()
                            }
                            loadState.append is LoadState.Error -> {
                                homeViewModel.handlePaginationAppendError(errorMessage, action)
                            }
                        }
                    }
                })
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun GameItem(game: GameResultEntity, gameClick: (Int) -> Unit) {
    Card(
        elevation = 20.dp,
        backgroundColor = EpicWorldTheme.colors.background,
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp))
            .height(250.dp)
            .fillMaxWidth()
            .clickable(
                enabled = true,
                onClick = {
                    gameClick(game.id)
                })
    ) {
        ConstraintLayout {
            val (image, title, rating) = createRefs()
            Image(
                contentScale = ContentScale.Crop,
                painter = rememberImagePainter(
                    data = game.backgroundImage,
                    builder = {
                        placeholder(R.drawable.ic_esports_placeholder_white)
                        crossfade(true)
                    }
                ),
                contentDescription = stringResource(id = R.string.all_game_image_description),
                modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .height(150.dp)
                    .fillMaxWidth()
            )
            Text(
                text = game.name,
                style = EpicWorldTheme.typography.title3,
                color = EpicWorldTheme.colors.primary,
                maxLines = 2,
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(image.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(rating) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Text(
                    text = game.rating.toString(),
                    style = EpicWorldTheme.typography.subTitle1,
                    color = EpicWorldTheme.colors.secondary,
                    modifier = Modifier
                        .padding(8.dp)
                )
                Image(
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = stringResource(id = R.string.all_star_rating),
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameItemPreview() {
    GameItem(
        game = GameResultEntity(123, "Max Payne", "", 4.5),
        gameClick = {  }
    )
}