package com.ruben.epicworld.presentation.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.games.GameResultEntity
import com.ruben.epicworld.domain.entity.games.GameResultsEntity
import com.ruben.epicworld.presentation.commonui.GetGamesError
import com.ruben.epicworld.presentation.commonui.LoadingView
import com.ruben.epicworld.presentation.commonui.NoResultsView
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import com.ruben.epicworld.presentation.theme.PlayFair
import com.ruben.epicworld.presentation.utility.LogCompositions
import com.ruben.epicworld.presentation.utility.setPortrait
import com.ruben.epicworld.presentation.utility.shouldPerformSearch

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
    LogCompositions(tag = "GameSearchScreen")

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(gameSearchViewModel.uiSideEffect()) {
        gameSearchViewModel.uiSideEffect().collect { sideEffect ->
            when (sideEffect) {
                is SearchSideEffect.NavigateToDetails -> {
                    navigateToDetails.invoke(sideEffect.id)
                }
            }
        }
    }

    DisposableEffect(true) {
        context.setPortrait()
        onDispose {
            keyboardController?.hide()
        }
    }

    val state by gameSearchViewModel.uiState().collectAsStateWithLifecycle()

    Column(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()) {
        SearchBar(
            keyboardController = keyboardController,
            onSearch = gameSearchViewModel::searchGame,
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
                    onSearchResultClicked = gameSearchViewModel::handleDetailsNavigation
                )
            }

            is SearchState.NoResultsState -> {
                NoResultsView()
            }

            is SearchState.ErrorState -> {
                GetGamesError(buttonClick = gameSearchViewModel::searchGame)
            }

            else -> {
                //do nothing
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchBar(
    keyboardController: SoftwareKeyboardController?,
    onSearch: (String) -> Unit,
    navigateBack: () -> Unit
) {

    LogCompositions(tag = "SearchBar")

    var searchState by remember {
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
            value = searchState,
            onValueChange = {
                if (searchState.text.trim() != it.text.trim() && it.text.trim()
                        .shouldPerformSearch()
                ) {
                    onSearch.invoke(it.text)
                }
                searchState = it
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
                if (searchState.text.isNotEmpty()) {
                    IconButton(onClick = {
                        searchState = TextFieldValue()
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
private fun SearchResults(results: GameResultsEntity, onSearchResultClicked: (Int) -> Unit) {
    LogCompositions(tag = "SearchResults")

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(items = results.gameResults, key = { item -> item.id }) {
            SearchItem(
                searchResult = it,
                onSearchResultClicked = onSearchResultClicked
            )
        }
    }
}

@Composable
private fun SearchItem(searchResult: GameResultEntity, onSearchResultClicked: (Int) -> Unit) {
    LogCompositions(tag = "SearchItem")

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
                else rememberAsyncImagePainter(
                    model = searchResult.backgroundImage,
                    placeholder = painterResource(id = R.drawable.ic_esports_placeholder_black)
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

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
private fun SearchBarPreview() {
    val keyboardController = LocalSoftwareKeyboardController.current
    SearchBar(
        keyboardController = keyboardController,
        onSearch = {},
        navigateBack = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchResultPreview() {
    SearchItem(
        searchResult = GameResultEntity(123, "Max Payne", "", 4.5),
        onSearchResultClicked = {}
    )
}