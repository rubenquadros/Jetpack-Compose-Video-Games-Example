package com.ruben.epicworld.presentation.videos

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.flowWithLifecycle
import coil.compose.rememberImagePainter
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.PlayerView
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import com.ruben.epicworld.domain.entity.gamevideos.VideoResultEntity
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.commonui.LoadingView
import com.ruben.epicworld.presentation.theme.*
import com.ruben.epicworld.presentation.utility.showToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Created by Ruben Quadros on 09/08/21
 **/
@ExperimentalAnimationApi
@Composable
fun GameVideosScreen(
    gameId: Int,
    navigateBack: () -> Unit,
    gameVideosViewModel: GameVideosViewModel = hiltViewModel()
) {
    HandleSideEffect(gameVideosViewModel.uiSideEffect())
    if (gameId == 0) {
        gameVideosViewModel.handleGameIdError()
        navigateBack.invoke()
    } else {
        gameVideosViewModel.getGameVideos(gameId = gameId)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val stateFlow = gameVideosViewModel.uiState()
    val stateFlowLifecycleAware = remember(lifecycleOwner, stateFlow) {
        stateFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val state by stateFlowLifecycleAware.collectAsState(initial = gameVideosViewModel.createInitialState())

    when (state.screenState) {
        ScreenState.Loading -> {
            LoadingView(modifier = Modifier.fillMaxSize())
        }
        ScreenState.Error -> {
            gameVideosViewModel.handleGameVideoError()
            navigateBack.invoke()
        }
        ScreenState.Success -> {
            state.gameVideos?.let {
                if (it.count == 0) {
                    gameVideosViewModel.handleNoGameVideos()
                    navigateBack.invoke()
                    return
                }
                ShowGameVideos(it)
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun ShowGameVideos(gameVideos: GameVideosEntity) {
    val playingIndex = remember {
        mutableStateOf(0)
    }

    fun onTrailerChange(index: Int) {
        playingIndex.value = index
    }

    Column {
        VideoPlayer(
            modifier = Modifier.weight(1f, fill = true),
            gameVideos = gameVideos.results,
            playingIndex = playingIndex,
            onTrailerChange = { newIndex -> onTrailerChange(newIndex) }
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
fun ShowTrailers(
    index: Int,
    trailer: VideoResultEntity,
    playingIndex: State<Int>,
    onTrailerClicked: (Int) -> Unit
) {
    val currentlyPlaying = remember {
        mutableStateOf(false)
    }
    currentlyPlaying.value = index == playingIndex.value
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
                colorFilter = if (trailer.preview.isEmpty()) ColorFilter.tint(White) else ColorFilter.tint(
                    PinkA400
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
            color = Black,
            textAlign = TextAlign.Center,
            softWrap = true,
            style = Typography.h4
        )
        if (currentlyPlaying.value) {
            Text(
                text = stringResource(id = R.string.game_videos_now_playing),
                color = PinkA400,
                textAlign = TextAlign.Center,
                style = Typography.h6,
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
        color = Gray300
    )
}

@ExperimentalAnimationApi
@Composable
fun VideoPlayer(
    gameVideos: List<VideoResultEntity>,
    playingIndex: State<Int>,
    onTrailerChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val visible = remember {
        mutableStateOf(true)
    }
    val videoTitle = remember {
        mutableStateOf(gameVideos[playingIndex.value].name)
    }
    val mediaItems = arrayListOf<MediaItem>()
    gameVideos.forEach {
        mediaItems.add(
            MediaItem.Builder().setUri(it.video).setMediaId(it.id.toString()).setTag(it)
                .setMediaMetadata(MediaMetadata.Builder().setDisplayTitle(it.name).build())
                .build()
        )
    }
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            this.setMediaItems(mediaItems)
            this.prepare()
            this.addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    if (player.contentPosition >= 200) visible.value = false
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    onTrailerChange(this@apply.currentPeriodIndex)
                    visible.value = true
                    videoTitle.value = mediaItem?.mediaMetadata?.displayTitle.toString()
                }
            })
        }
    }

    exoPlayer.seekTo(playingIndex.value, C.TIME_UNSET)
    exoPlayer.playWhenReady = true

    LocalLifecycleOwner.current.lifecycle.addObserver(object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun resumeVideo() {
            if (exoPlayer.isPlaying.not()) {
                exoPlayer.play()
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun stopVideo() {
            exoPlayer.pause()
        }
    })

    ConstraintLayout(modifier = modifier.background(Black)) {
        val (title, videoPlayer) = createRefs()
        AnimatedVisibility(
            visible = visible.value,
            enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(animationSpec = tween(durationMillis = 250)),
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text(
                text = videoTitle.value,
                style = Typography.h6,
                color = White,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
        DisposableEffect(
            AndroidView(
                modifier = modifier
                    .testTag("VideoPlayer")
                    .constrainAs(videoPlayer) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    }
                })
        ) {
            onDispose {
                exoPlayer.release()
            }
        }
    }
}

@Composable
fun HandleSideEffect(sideEffectFlow: Flow<GameVideosSideEffect>) {
    val context = LocalContext.current
    val gameIdError = stringResource(id = R.string.game_videos_invalid_game_id)
    val gameVideosError = stringResource(id = R.string.all_generic_error)
    val noGameVideos = stringResource(id = R.string.game_videos_no_videos)
    LaunchedEffect(sideEffectFlow) {
        sideEffectFlow.collect { uiSideEffect ->
            when (uiSideEffect) {
                is GameVideosSideEffect.ShowGameIdErrorToast -> {
                    context.showToast(gameIdError)
                }
                is GameVideosSideEffect.ShowGameVideosErrorToast -> {
                    context.showToast(gameVideosError)
                }
                is GameVideosSideEffect.ShowNoGameVideosToast -> {
                    context.showToast(noGameVideos)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowTrailerPreview() {
    ShowTrailers(
        index = 0,
        trailer = VideoResultEntity(1, "", "GTA Online: DeadLine GTA Online: DeadLine", ""),
        playingIndex = remember { mutableStateOf(0) },
        onTrailerClicked = { }
    )
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun ShowGameVideosPreview() {
    ShowGameVideos(gameVideos = GameVideosEntity(3, arrayListOf()))
}