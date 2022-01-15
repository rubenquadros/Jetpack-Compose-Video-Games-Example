package com.ruben.epicworld.presentation.filters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.flowlayout.FlowRow
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.filters.PlatformFilterEntity
import com.ruben.epicworld.domain.entity.filters.SortFilterEntity
import com.ruben.epicworld.domain.entity.genres.GenresResultEntity
import com.ruben.epicworld.presentation.commonui.Chip
import com.ruben.epicworld.presentation.commonui.ErrorView
import com.ruben.epicworld.presentation.commonui.LoadingView
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ruben Quadros on 15/01/22
 **/
@Composable
fun GameFiltersScreen(
    gameFiltersViewModel: GameFiltersViewModel = hiltViewModel()
) {
    HandleSideEffect(sideEffectFlow = gameFiltersViewModel.uiSideEffect())

    val lifecycleOwner = LocalLifecycleOwner.current
    val stateFlow = gameFiltersViewModel.uiState()
    val stateLifecycleAware = remember(lifecycleOwner, stateFlow) {
        stateFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val state by stateLifecycleAware.collectAsState(initial = gameFiltersViewModel.createInitialState())

    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is GameFiltersState.LoadingState -> {
                LoadingView(modifier = Modifier
                    .padding(top = 32.dp)
                    .align(Alignment.TopCenter))
            }
            is GameFiltersState.FilterState -> {
                (state as? GameFiltersState.FilterState)?.let {
                    FiltersContent {
                        GameFilters(
                            modifier = Modifier.padding(16.dp),
                            sortOrders = it.sortOrders,
                            platforms = it.platforms,
                            genres = it.genres
                        )
                    }
                }
            }
            is GameFiltersState.ErrorState -> {
                FiltersContent(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                ) {
                    ErrorView(
                        modifier = Modifier.padding(top = 32.dp),
                        buttonClick = { gameFiltersViewModel.initData() }
                    )
                }
            }
            else -> { /* do nothing */ }
        }
    }
}

@Composable
fun FiltersContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        FilterHeader()
        content()
    }
}

@Composable
fun FilterHeader() {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth(),
        constraintSet = ConstraintSet {
            val title = createRefFor("title")
            val divider = createRefFor("divider")
            val doneButton = createRefFor("done_button")
            val clearButton = createRefFor("clear_button")

            constrain(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start, margin = 16.dp)
                bottom.linkTo(divider.top)
            }

            constrain(doneButton) {
                top.linkTo(parent.top)
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(divider.top)
            }

            constrain(clearButton) {
                top.linkTo(parent.top)
                end.linkTo(doneButton.start, margin = 16.dp)
                bottom.linkTo(divider.top)
            }

            constrain(divider) {
                start.linkTo(parent.start, margin = 18.dp)
                end.linkTo(parent.end, margin = 18.dp)
                bottom.linkTo(parent.bottom, margin = 8.dp)
                width = Dimension.fillToConstraints
                height = Dimension.value(1.dp)
            }
        }
    ) {
        Text(
            modifier = Modifier.layoutId("title"),
            text = stringResource(id = R.string.game_filters_title),
            color = EpicWorldTheme.colors.onBackground,
            style = EpicWorldTheme.typography.title3,
        )

        TextButton(
            modifier = Modifier.layoutId("done_button"),
            onClick = {}
        ) {
            Text(
                text = stringResource(id = R.string.all_done),
                color = EpicWorldTheme.colors.secondary,
                style = EpicWorldTheme.typography.subTitle1,
            )
        }

        Box(
            modifier = Modifier.layoutId("clear_button")
        ) {
            AnimatedVisibility(visible = true) {
                TextButton(
                    onClick = {}
                ) {
                    Text(
                        text = stringResource(id = R.string.all_clear),
                        color = EpicWorldTheme.colors.secondary,
                        style = EpicWorldTheme.typography.subTitle1,
                    )
                }
            }
        }

        Box(modifier = Modifier
            .layoutId("divider")
            .background(color = EpicWorldTheme.colors.onDisabled)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameFilters(
    modifier: Modifier = Modifier,
    sortOrders: List<SortFilterEntity>,
    platforms: List<PlatformFilterEntity>,
    genres: List<GenresResultEntity>
) {
    LazyColumn(modifier = modifier) {
        stickyHeader {
            FilterStickyHeader(title = stringResource(id = R.string.game_filters_sort_by))
        }

        item {
            FlowRow {
                sortOrders.forEach {
                    Chip(
                        modifier = Modifier.padding(8.dp),
                        chipId = it.value,
                        content = {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = it.name,
                                color = EpicWorldTheme.colors.onBackground
                            )
                        },
                        onValueChange = {}
                    )
                }
            }
        }

        stickyHeader {
            FilterStickyHeader(title = stringResource(id = R.string.game_filters_platforms))
        }

        item {
            FlowRow {
                platforms.forEach {
                    Chip(
                        modifier = Modifier.padding(8.dp),
                        chipId = it.id,
                        content = {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = it.name,
                                color = EpicWorldTheme.colors.onBackground
                            )
                        },
                        onValueChange = {}
                    )
                }
            }
        }

        stickyHeader {
            FilterStickyHeader(title = stringResource(id = R.string.game_filters_genres))
        }

        item {
            FlowRow {
                genres.forEach {
                    Chip(
                        modifier = Modifier.padding(8.dp),
                        chipId = it.id,
                        content = {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = it.name,
                                color = EpicWorldTheme.colors.onBackground
                            )
                        },
                        onValueChange = {}
                    )
                }
            }
        }
    }
}

@Composable
fun FilterStickyHeader(title: String) {
    Box(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = title,
            color = EpicWorldTheme.colors.onBackground,
            style = EpicWorldTheme.typography.subTitle2
        )
    }
}

@Composable
fun HandleSideEffect(sideEffectFlow: Flow<GameFiltersSideEffect>) {
    LaunchedEffect(sideEffectFlow) {
        sideEffectFlow.collect {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFilterHeader() {
    FilterHeader()
}

@Preview(showBackground = true)
@Composable
fun PreviewFilterStickyHeader() {
    FilterStickyHeader(title = stringResource(id = R.string.game_filters_sort_by))
}

@Preview(showBackground = true)
@Composable
fun PreviewFiltersContent() {
    FiltersContent {
        GameFilters(
            modifier = Modifier.padding(top = 16.dp),
            sortOrders = listOf(
                SortFilterEntity(
                    name = "Name",
                    value = "name"
                ),
                SortFilterEntity(
                    name = "Released Date",
                    value = "released"
                ),
                SortFilterEntity(
                    name = "Rating",
                    value = "rating"
                ),
                SortFilterEntity(
                    name = "Critics Score",
                    value = "metacritic"
                )
            ),
            platforms = listOf(
                PlatformFilterEntity(
                    name = "PC",
                    id = 1
                ),
                PlatformFilterEntity(
                    name = "PlayStation",
                    id = 2
                ),
                PlatformFilterEntity(
                    name = "Xbox",
                    id = 3
                )
            ),
            genres = listOf(
                GenresResultEntity(
                    id = 1,
                    name = "Action",
                    gamesCount = 1,
                    imageBackground = "",
                    slug = ""
                ),
                GenresResultEntity(
                    id = 2,
                    name = "Indie",
                    gamesCount = 2,
                    imageBackground = "",
                    slug = ""
                ),
                GenresResultEntity(
                    id = 3,
                    name = "Adventure",
                    gamesCount = 3,
                    imageBackground = "",
                    slug = ""
                ),
                GenresResultEntity(
                    id = 4,
                    name = "RPG",
                    gamesCount = 4,
                    imageBackground = "",
                    slug = ""
                ),
            )
        )
    }
}