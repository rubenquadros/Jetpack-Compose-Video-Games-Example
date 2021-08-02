package com.ruben.epicworld.presentation.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.games.AddedByStatusEntity
import com.ruben.epicworld.domain.entity.games.EsrbRatingEntity
import com.ruben.epicworld.domain.entity.games.GameResultsEntity
import com.ruben.epicworld.presentation.commonui.HomeAppBar
import com.ruben.epicworld.presentation.commonui.LoadingItem
import com.ruben.epicworld.presentation.home.HomeViewModel
import com.ruben.epicworld.presentation.theme.*
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Ruben Quadros on 01/08/21
 **/

@ExperimentalFoundationApi
@Composable
fun HomeScreen(
    uiState: StateFlow<HomeState>,
    searchClick: () -> Unit,
    filterClick: () -> Unit,
    gameClick: () -> Unit
) {
    Scaffold(topBar = {
        HomeAppBar(
            title = stringResource(id = R.string.home_app_bar_title),
            searchClick = searchClick,
            filterClick = filterClick
        )
    },
        content = { GameListing(uiState = uiState, gameClick = gameClick) }
    )
}

@ExperimentalFoundationApi
@Composable
fun GameListing(uiState: StateFlow<HomeState>, gameClick: () -> Unit) {

    val state = uiState.collectAsState()
    when (state.value) {
        is HomeState.InitialState -> {
            //do nothing
        }
        is HomeState.LoadingState -> {
            //do nothing
        }
        is HomeState.AllGamesData -> {
            val lazyGameItems = (state.value as HomeState.AllGamesData).games.collectAsLazyPagingItems()
            LazyVerticalGrid(cells = GridCells.Fixed(2), content = {
                items(lazyGameItems.itemCount) { index ->
                    lazyGameItems[index]?.let {
                        GameItem(game = it, gameClick = gameClick)
                    }
                }

                lazyGameItems.apply {
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

                        }
                        loadState.append is LoadState.Error -> {

                        }
                    }
                }
            })

        }
    }
}

@Composable
fun GameItem(game: GameResultsEntity, gameClick: () -> Unit) {
    Card(
        elevation = 20.dp,
        backgroundColor = Black,
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp))
            .height(250.dp)
            .fillMaxWidth()
            .clickable(enabled = true, onClick = gameClick)
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
                    contentDescription ="",
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun GameItemPreview() {
    GameItem(
        game = GameResultsEntity(
            123,
            "abc",
            "Max Payne",
            "2013-09-17",
            false,
            "https://media.rawg.io/media/games/84d/84da2ac3fdfc6507807a1808595afb12.jpg",
            4.48,
            5,
            4908,
            arrayListOf(),
            33,
            15375,
            AddedByStatusEntity(1, 2, 3, 4, 5, 6),
            97,
            79,
            410,
            "2021-03-03T20:31:29",
            "",
            4963,
            "",
            "",
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            "",
            arrayListOf(),
            EsrbRatingEntity(1, "", ""),
            arrayListOf()
        )
    ) {
        //do nothing
    }
}