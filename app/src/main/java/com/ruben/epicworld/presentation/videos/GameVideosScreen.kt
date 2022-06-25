package com.ruben.epicworld.presentation.videos

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import coil.compose.rememberImagePainter
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import com.ruben.epicworld.domain.entity.gamevideos.VideoResultEntity
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.commonui.LoadingView
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import com.ruben.epicworld.presentation.utility.findActivity
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

    var visible by remember {
        mutableStateOf(true)
    }
    var videoTitle by remember {
        mutableStateOf(gameVideos.results[playingIndex].name)
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
        ExoPlayer.Builder(context).build().apply {
            this.setMediaItems(mediaItems)
            this.prepare()
            this.addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    if (player.contentPosition >= 200) visible = false
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    onTrailerChange(currentPeriodIndex)
                    visible = true
                    videoTitle = mediaItem?.mediaMetadata?.displayTitle.toString()
                }
            })
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
        getVideoTitle = { videoTitle },
        isVisible = { visible },
        playingIndex = playingIndex,
        gameVideos = gameVideos,
        onTrailerChange = { index -> onTrailerChange(index) }
    )
}

@Composable
fun GameVideos(
    exoPlayer: Player,
    getVideoTitle: () -> String,
    playingIndex: Int,
    isVisible: () -> Boolean,
    gameVideos: GameVideosEntity,
    onTrailerChange: (Int) -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    fun handleOrientation(isFullScreen: Boolean) {
        context.findActivity()?.requestedOrientation = if (isFullScreen) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    val playerView: View = remember {
        StyledPlayerView(context).apply {
            player = exoPlayer
            setFullscreenButtonClickListener { isFullScreen ->
                handleOrientation(isFullScreen)
            }
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            PortraitView(
                playerView = playerView,
                getVideoTitle = getVideoTitle,
                isVisible = isVisible,
                playingIndex = playingIndex,
                onTrailerChange = { index -> onTrailerChange(index) },
                gameVideos = gameVideos
            )
        }
        else -> {
            LandscapeView(
                playerView = playerView,
                getVideoTitle = getVideoTitle,
                isVisible = isVisible
            )
        }
    }
}

@Composable
fun PortraitView(
    playerView: View,
    getVideoTitle: () -> String,
    isVisible: () -> Boolean,
    gameVideos: GameVideosEntity,
    playingIndex: Int,
    onTrailerChange: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        VideoPlayer(
            modifier = Modifier.weight(1f, fill = true),
            playerView = playerView,
            getVideoTitle = getVideoTitle,
            isVisible = isVisible
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
fun LandscapeView(playerView: View, getVideoTitle: () -> String, isVisible: () -> Boolean) {
    Box(modifier = Modifier.fillMaxSize()) {
        VideoPlayer(
            modifier = Modifier.fillMaxSize(),
            playerView = playerView,
            getVideoTitle = getVideoTitle,
            isVisible = isVisible
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

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    playerView: View,
    getVideoTitle: () -> String,
    isVisible: () -> Boolean,
) {
    val videoTitle = remember(key1 = getVideoTitle()) {
        getVideoTitle()
    }

    val visible = remember(key1 = isVisible()) {
        isVisible()
    }

    ConstraintLayout(modifier = modifier.background(EpicWorldTheme.colors.background)) {
        val (title, videoPlayer) = createRefs()
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(animationSpec = tween(durationMillis = 250)),
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text(
                text = videoTitle,
                style = EpicWorldTheme.typography.subTitle2,
                color = EpicWorldTheme.colors.onBackground,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
        AndroidView(
            modifier = Modifier
                .testTag("VideoPlayer")
                .constrainAs(videoPlayer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            factory = {
                playerView
            })
    }
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