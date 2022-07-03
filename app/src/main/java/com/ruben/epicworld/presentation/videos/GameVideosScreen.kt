package com.ruben.epicworld.presentation.videos

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import coil.compose.rememberImagePainter
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import com.ruben.epicworld.domain.entity.gamevideos.PlayerWrapper
import com.ruben.epicworld.domain.entity.gamevideos.VideoResultEntity
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.commonui.LoadingView
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import com.ruben.epicworld.presentation.utility.Constants.PLAYER_SEEK_BACK_INCREMENT
import com.ruben.epicworld.presentation.utility.Constants.PLAYER_SEEK_FORWARD_INCREMENT
import com.ruben.epicworld.presentation.utility.LogCompositions
import com.ruben.epicworld.presentation.utility.showToast

/**
 * Created by Ruben Quadros on 09/08/21
 **/
@Composable
fun GameVideosScreen(
    navigateBack: () -> Unit,
    gameVideosViewModel: GameVideosViewModel = hiltViewModel()
) {
    LogCompositions(tag = "GameVideosScreen")

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val stateFlow = gameVideosViewModel.uiState()
    val stateFlowLifecycleAware = remember(lifecycleOwner, stateFlow) {
        stateFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val state by stateFlowLifecycleAware.collectAsState(initial = gameVideosViewModel.createInitialState())

    val gameIdError = stringResource(id = R.string.game_videos_invalid_game_id)
    val gameVideosError = stringResource(id = R.string.all_generic_error)
    val noGameVideos = stringResource(id = R.string.game_videos_no_videos)

    LaunchedEffect(gameVideosViewModel.uiSideEffect()) {
        gameVideosViewModel.uiSideEffect().collect { uiSideEffect ->
            when (uiSideEffect) {
                GameVideosSideEffect.ShowGameIdErrorToast -> {
                    context.showToast(gameIdError)
                    navigateBack.invoke()
                }
                GameVideosSideEffect.GameVideosError -> {
                    context.showToast(gameVideosError)
                    navigateBack.invoke()
                }
                GameVideosSideEffect.ShowNoGameVideosToast -> {
                    context.showToast(noGameVideos)
                }
            }
        }
    }

    when (state.screenState) {
        ScreenState.Loading -> {
            LoadingView(modifier = Modifier.fillMaxSize())
        }
        ScreenState.Error -> {
            //navigate back
        }
        ScreenState.Success -> {
            state.gameVideos?.let {
                if (it.count == 0) {
                    gameVideosViewModel.handleNoGameVideos()
                    navigateBack.invoke()
                    return
                }
                ShowGameVideos(
                    getGameVideosEntity = { it }
                )
            }
        }
    }
}

@Composable
private fun ShowGameVideos(
    getGameVideosEntity: () -> GameVideosEntity
) {
    LogCompositions(tag = "ShowGameVideos")

    val context = LocalContext.current

    val gameVideos = remember(getGameVideosEntity()) {
        getGameVideosEntity()
    }

    val mediaItems = arrayListOf<MediaItem>()
    gameVideos.results.forEach {
        mediaItems.add(
            MediaItem.Builder().setUri(it.video).setMediaId(it.id.toString()).setTag(it)
                .setMediaMetadata(MediaMetadata.Builder().setDisplayTitle(it.name).build())
                .build()
        )
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setSeekBackIncrementMs(PLAYER_SEEK_BACK_INCREMENT)
            .setSeekForwardIncrementMs(PLAYER_SEEK_FORWARD_INCREMENT)
            .build().apply {
                this.setMediaItems(mediaItems)
                this.prepare()
                this.playWhenReady = true
            }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(key1 = Unit) {
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                if (exoPlayer.isPlaying.not()) {
                    exoPlayer.play()
                }
            }

            override fun onStop(owner: LifecycleOwner) {
                exoPlayer.pause()
                super.onStop(owner)
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    GameVideos(
        playerWrapper = PlayerWrapper(exoPlayer),
        gameVideos = gameVideos
    )
}

@Composable
private fun GameVideos(
    playerWrapper: PlayerWrapper,
    gameVideos: GameVideosEntity,
) {
    LogCompositions(tag = "GameVideos")

    val configuration = LocalConfiguration.current

    var playingIndex by remember {
        mutableStateOf(0)
    }

    fun onTrailerChange(index: Int) {
        playingIndex = index
        playerWrapper.exoPlayer.seekTo(playingIndex, C.TIME_UNSET)
        playerWrapper.exoPlayer.playWhenReady = true
    }

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            PortraitView(
                playerWrapper = playerWrapper,
                playingIndex = playingIndex,
                onTrailerChange = { index -> onTrailerChange(index) },
                gameVideos = gameVideos
            )
        }
        else -> {
            LandscapeView(
                playerWrapper = playerWrapper,
            )
        }
    }
}

@Composable
private fun PortraitView(
    playerWrapper: PlayerWrapper,
    gameVideos: GameVideosEntity,
    playingIndex: Int,
    onTrailerChange: (Int) -> Unit
) {
    LogCompositions(tag = "PortraitView")

    Column {
        PlayerView(
            modifier = Modifier.weight(1f, fill = true),
            playerWrapper = playerWrapper,
            isFullScreen = false,
            onTrailerChange = onTrailerChange
        )
        LazyColumn(
            modifier = Modifier.weight(1f, fill = true),
            content = {
                itemsIndexed(gameVideos.results) { index, trailer ->
                    ShowTrailers(
                        index = index,
                        trailer = trailer,
                        playingIndex = playingIndex,
                        onTrailerClicked = { newIndex -> onTrailerChange(newIndex) })
                }
            })
    }
}

@Composable
private fun LandscapeView(playerWrapper: PlayerWrapper,) {
    LogCompositions(tag = "LandscapeView")

    Box(modifier = Modifier.fillMaxSize()) {
        PlayerView(
            modifier = Modifier.fillMaxSize(),
            playerWrapper = playerWrapper,
            isFullScreen = true
        )
    }
}

@Composable
private fun ShowTrailers(
    index: Int,
    trailer: VideoResultEntity,
    playingIndex: Int,
    onTrailerClicked: (Int) -> Unit
) {
    LogCompositions(tag = "ShowTrailers")

    var currentlyPlaying by remember {
        mutableStateOf(false)
    }

    currentlyPlaying = index == playingIndex

    ConstraintLayout(modifier = Modifier
        .testTag("TrailerParent")
        .padding(8.dp)
        .wrapContentSize()
        .clickable {
            onTrailerClicked(index)
        }) {
        val (thumbnail, play, title, nowPlaying) = createRefs()
        Image(
            contentScale = ContentScale.Crop,
            painter = rememberImagePainter(data = trailer.preview, builder = {
                placeholder(R.drawable.app_logo)
                crossfade(true)
            }),
            contentDescription = stringResource(id = R.string.game_videos_trailer),
            modifier = Modifier
                .height(120.dp)
                .width(120.dp)
                .clip(RoundedCornerShape(20.dp))
                .shadow(elevation = 20.dp)
                .constrainAs(thumbnail) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(parent.start, margin = 8.dp)
                    bottom.linkTo(parent.bottom)
                }
        )
        if (currentlyPlaying) {
            Image(
                contentScale = ContentScale.Crop,
                colorFilter = if (trailer.preview.isEmpty()) ColorFilter.tint(EpicWorldTheme.colors.onBackground) else ColorFilter.tint(
                    EpicWorldTheme.colors.primary
                ),
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = stringResource(id = R.string.game_videos_play),
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .graphicsLayer {
                        clip = true
                        shadowElevation = 20.dp.toPx()
                    }
                    .constrainAs(play) {
                        top.linkTo(thumbnail.top)
                        start.linkTo(thumbnail.start)
                        end.linkTo(thumbnail.end)
                        bottom.linkTo(thumbnail.bottom)
                    }
            )

            Text(
                text = stringResource(id = R.string.game_videos_now_playing),
                color = EpicWorldTheme.colors.primary,
                textAlign = TextAlign.Center,
                style = EpicWorldTheme.typography.subTitle2,
                modifier = Modifier.constrainAs(nowPlaying) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    start.linkTo(thumbnail.end, margin = 8.dp)
                    bottom.linkTo(thumbnail.bottom, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    width = Dimension.preferredWrapContent
                    height = Dimension.preferredWrapContent
                }
            )
        }
        Text(
            text = trailer.name,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(thumbnail.top, margin = 8.dp)
                    start.linkTo(thumbnail.end, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    width = Dimension.preferredWrapContent
                    height = Dimension.wrapContent
                },
            color = EpicWorldTheme.colors.background,
            textAlign = TextAlign.Center,
            softWrap = true,
            style = EpicWorldTheme.typography.subTitle1
        )

        Divider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .testTag("Divider"),
            color = EpicWorldTheme.colors.surface
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShowTrailerPreview() {
    ShowTrailers(
        index = 0,
        trailer = VideoResultEntity(1, "", "GTA Online: DeadLine GTA Online: DeadLine", ""),
        playingIndex = 0,
        onTrailerClicked = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun ShowGameVideosPreview() {
    ShowGameVideos(
        getGameVideosEntity = { GameVideosEntity(3, emptyList()) }
    )
}