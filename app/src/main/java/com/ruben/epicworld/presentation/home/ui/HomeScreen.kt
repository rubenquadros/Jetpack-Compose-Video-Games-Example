package com.ruben.epicworld.presentation.home.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.games.GameResultsEntity
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.commonui.HomeAppBar
import com.ruben.epicworld.presentation.commonui.LoadingItem
import com.ruben.epicworld.presentation.commonui.SnackbarView
import com.ruben.epicworld.presentation.home.HomeViewModel
import com.ruben.epicworld.presentation.theme.AmberA400
import com.ruben.epicworld.presentation.theme.Black
import com.ruben.epicworld.presentation.theme.PinkA400
import com.ruben.epicworld.presentation.theme.Typography
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

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
    HandleSideEffect(homeViewModel.uiSideEffect(), scaffoldState)
    Scaffold(topBar = {
        HomeAppBar(
            title = stringResource(id = R.string.home_app_bar_title),
            searchClick = { openSearch.invoke() },
            filterClick = {  }
        )
    },
        scaffoldState = scaffoldState,
        content = { GameListing(openGameDetails = openGameDetails, homeViewModel = homeViewModel) }
    )
}

@ExperimentalFoundationApi
@Composable
fun GameListing(openGameDetails: (Int) -> Unit, homeViewModel: HomeViewModel) {
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
            ErrorItem { homeViewModel.initData() }
        }
        is ScreenState.Success -> {
            val lazyGameItems = state.games?.collectAsLazyPagingItems()
            Log.d("@@@", lazyGameItems?.itemCount.toString())
            lazyGameItems?.let { gameItems ->
                LazyVerticalGrid(cells = GridCells.Fixed(2), content = {
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

@Composable
fun GameItem(game: GameResultsEntity, gameClick: (Int) -> Unit) {
    Card(
        elevation = 20.dp,
        backgroundColor = Black,
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
                        placeholder(R.drawable.ic_esports_placeholder)
                        crossfade(true)
                    }
                ),
                contentDescription = stringResource(id = R.string.home_screen_game_image_description),
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
                style = Typography.h3,
                color = PinkA400,
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
                    style = Typography.h4,
                    color = AmberA400,
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

@Composable
fun ErrorItem(buttonClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = R.string.home_screen_error_message),
            textAlign = TextAlign.Center,
            color = Black,
            style = Typography.h5
        )
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = buttonClick
        ) {
            Text(
                text = stringResource(id = R.string.home_screen_retry),
                style = Typography.button
            )
        }
    }
}

@Composable
fun HandleSideEffect(uiSideEffectFlow: Flow<HomeSideEffect>, scaffoldState: ScaffoldState) {
    LaunchedEffect(uiSideEffectFlow) {
        val messageHost = SnackbarView(this)
        uiSideEffectFlow.collect { uiSideEffect ->
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
}

@Preview(showBackground = true)
@Composable
fun GameItemPreview() {
    GameItem(
        game = GameResultsEntity(123, "Max Payne", "", 4.5),
        gameClick = {  }
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorItemPreview() {
    ErrorItem {
        //do nothing
    }
}