package com.ruben.epicworld.presentation.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.games.GameResultsEntity
import com.ruben.epicworld.presentation.commonui.GetGamesError
import com.ruben.epicworld.presentation.commonui.LoadingView
import com.ruben.epicworld.presentation.commonui.NoResultsView
import com.ruben.epicworld.presentation.theme.*
import com.ruben.epicworld.presentation.utility.shouldPerformSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Created by Ruben Quadros on 12/09/21
 **/
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GameSearchScreen(
    gameSearchViewModel: GameSearchViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToDetails: (Int) -> Unit
) {

    HandleSideEffect(
        sideEffectFlow = gameSearchViewModel.uiSideEffect(),
        navigateToDetails = navigateToDetails
    )

    val keyboardController = LocalSoftwareKeyboardController.current

    DisposableEffect(true) {
        onDispose {
            keyboardController?.hide()
        }
    }

    fun onSearch(query: String) {
        gameSearchViewModel.searchGame(query)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val stateFlow = gameSearchViewModel.uiState()
    val stateFlowLifecycleAware = remember(lifecycleOwner, stateFlow) {
        stateFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val state by stateFlowLifecycleAware.collectAsState(initial = gameSearchViewModel.createInitialState())

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            keyboardController = keyboardController,
            onSearch = { query -> onSearch(query) },
            navigateBack = navigateBack
        )

        when (state) {
            is SearchState.LoadingState -> {
                LoadingView(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            is SearchState.SearchResultState -> {
                val results = (state as SearchState.SearchResultState).searchResults
                SearchResults(
                    results = results,
                    onSearchResultClicked = { id -> gameSearchViewModel.handleDetailsNavigation(id) }
                )
            }

            is SearchState.NoResultsState -> {
                NoResultsView()
            }

            is SearchState.ErrorState -> {
                GetGamesError { gameSearchViewModel.searchGame() }
            }

            else -> {
                //do nothing
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    keyboardController: SoftwareKeyboardController?,
    onSearch: (String) -> Unit,
    navigateBack: () -> Unit
) {

    val searchState = remember {
        mutableStateOf(TextFieldValue())
    }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .testTag("Search Bar"),
            value = searchState.value,
            onValueChange = {
                if (searchState.value.text.trim() != it.text.trim() && it.text.trim()
                        .shouldPerformSearch()
                ) {
                    onSearch.invoke(it.text)
                }
                searchState.value = it
            },
            placeholder = {
                Text(
                    modifier = Modifier.background(Color.Transparent),
                    text = stringResource(id = R.string.game_search_suggestion),
                    color = EpicWorldTheme.colors.background,
                    style = EpicWorldTheme.typography.body2
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = EpicWorldTheme.colors.onBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = EpicWorldTheme.colors.primary
            ),
            textStyle = TextStyle(
                color = EpicWorldTheme.colors.background,
                fontFamily = PlayFair,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            leadingIcon = {
                IconButton(onClick = {
                    focusRequester.freeFocus()
                    navigateBack.invoke()
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = stringResource(id = R.string.all_back_button)
                    )
                }
            },
            trailingIcon = {
                if (searchState.value.text.isNotEmpty()) {
                    IconButton(onClick = {
                        searchState.value = TextFieldValue()
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = stringResource(id = R.string.all_clear_button)
                        )
                    }
                }
            }
        )

        Box(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(EpicWorldTheme.colors.primaryVariant)
            .testTag("Bottom Border")
        )
    }
}

@Composable
fun SearchResults(results: List<GameResultsEntity>, onSearchResultClicked: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(items = results, key = { item -> item.id }) {
            SearchItem(
                searchResult = it,
                onSearchResultClicked = onSearchResultClicked
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SearchItem(searchResult: GameResultsEntity, onSearchResultClicked: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable {
                onSearchResultClicked.invoke(searchResult.id)
            }
            .testTag("Search Result Parent")
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .height(40.dp)
                .fillMaxWidth()
        ) {
            Image(
                contentScale = ContentScale.Crop,
                painter = if (searchResult.backgroundImage.isEmpty()) painterResource(id = R.drawable.ic_esports_placeholder_black)
                else rememberImagePainter(
                    data = searchResult.backgroundImage,
                    builder = {
                        placeholder(R.drawable.ic_esports_placeholder_black)
                        crossfade(true)
                    }
                ),
                contentDescription = stringResource(id = R.string.all_game_image_description),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically)
            )

            Text(
                text = searchResult.name,
                style = EpicWorldTheme.typography.body1,
                color = EpicWorldTheme.colors.background,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
            )

        }

        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(0.5.dp)
                .fillMaxWidth()
                .background(EpicWorldTheme.colors.onDisabled)
                .testTag("Search Divider")
        )
    }
}

@Composable
fun HandleSideEffect(sideEffectFlow: Flow<SearchSideEffect>, navigateToDetails: (Int) -> Unit) {
    LaunchedEffect(sideEffectFlow) {
        sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is SearchSideEffect.NavigateToDetails -> {
                    navigateToDetails.invoke(sideEffect.id)
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun SearchBarPreview() {
    val keyboardController = LocalSoftwareKeyboardController.current
    SearchBar(
        keyboardController = keyboardController,
        onSearch = {},
        navigateBack = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SearchResultPreview() {
    SearchItem(
        searchResult = GameResultsEntity(123, "Max Payne", "", 4.5),
        onSearchResultClicked = {}
    )
}