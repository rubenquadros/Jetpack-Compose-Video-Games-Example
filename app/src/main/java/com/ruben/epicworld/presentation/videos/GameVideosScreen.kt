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
import com.google.android.exoplayer2.*
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import com.ruben.epicworld.domain.entity.gamevideos.VideoResultEntity
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.commonui.LoadingView
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import com.ruben.epicworld.presentation.utility.Constants.PLAYER_SEEK_BACK_INCREMENT
import com.ruben.epicworld.presentation.utility.Constants.PLAYER_SEEK_FORWARD_INCREMENT
import com.ruben.epicworld.presentation.utility.showToast

/**
 * Created by Ruben Quadros on 09/08/21
 **/
@Composable
fun GameVideosScreen(
    navigateBack: () -> Unit,
    gameVideosViewModel: GameVideosViewModel = hiltViewModel()
) {
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
fun ShowGameVideos(
    getGameVideosEntity: () -> GameVideosEntity
) {
    val context = LocalContext.current

    var playingIndex by remember {
        mutableStateOf(0)
    }

    fun onTrailerChange(index: Int) {
        playingIndex = index
    }

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
            }
    }

    exoPlayer.seekTo(playingIndex, C.TIME_UNSET)
    exoPlayer.playWhenReady = true

    LocalLifecycleOwner.current.lifecycle.addObserver(object : DefaultLifecycleObserver {

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
    })

    DisposableEffect(key1 = Unit) {
        onDispose { exoPlayer.release() }
    }

    GameVideos(
        exoPlayer = exoPlayer,
        playingIndex = playingIndex,
        gameVideos = gameVideos,
        onTrailerChange = { index -> onTrailerChange(index) }
    )
}

@Composable
fun GameVideos(
    exoPlayer: Player,
    playingIndex: Int,
    gameVideos: GameVideosEntity,
    onTrailerChange: (Int) -> Unit
) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            PortraitView(
                exoPlayer = exoPlayer,
                playingIndex = playingIndex,
                onTrailerChange = { index -> onTrailerChange(index) },
                gameVideos = gameVideos
            )
        }
        else -> {
            LandscapeView(
                exoPlayer = exoPlayer
            )
        }
    }
}

@Composable
fun PortraitView(
    exoPlayer: Player,
    gameVideos: GameVideosEntity,
    playingIndex: Int,
    onTrailerChange: (Int) -> Unit
) {
    Column {
        PlayerView(
            modifier = Modifier.weight(1f, fill = true),
            exoPlayer = exoPlayer,
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
fun LandscapeView(exoPlayer: Player) {
    Box(modifier = Modifier.fillMaxSize()) {
        PlayerView(
            modifier = Modifier.fillMaxSize(),
            exoPlayer = exoPlayer,
            isFullScreen = true
        )
    }
}

@Composable
fun ShowTrailers(
    index: Int,
    trailer: VideoResultEntity,
    playingIndex: Int,
    onTrailerClicked: (Int) -> Unit
) {
    val currentlyPlaying = remember {
        mutableStateOf(false)
    }
    currentlyPlaying.value = index == playingIndex
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
        if (currentlyPlaying.value) {
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
        if (currentlyPlaying.value) {
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
        TrailerDivider()
    }
}

@Composable
fun TrailerDivider() {
    Divider(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .testTag("Divider"),
        color = EpicWorldTheme.colors.surface
    )
}

@Preview(showBackground = true)
@Composable
fun ShowTrailerPreview() {
    ShowTrailers(
        index = 0,
        trailer = VideoResultEntity(1, "", "GTA Online: DeadLine GTA Online: DeadLine", ""),
        playingIndex = 0,
        onTrailerClicked = { }
    )
}

@Preview(showBackground = true)
@Composable
fun ShowGameVideosPreview() {
    ShowGameVideos(
        getGameVideosEntity = { GameVideosEntity(3, arrayListOf()) }
    )
}