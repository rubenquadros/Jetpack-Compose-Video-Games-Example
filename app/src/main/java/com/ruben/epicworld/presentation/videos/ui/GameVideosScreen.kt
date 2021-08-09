package com.ruben.epicworld.presentation.videos.ui

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.ruben.epicworld.R
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import com.ruben.epicworld.domain.entity.gamevideos.VideoResultEntity
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.commonui.LoadingView
import com.ruben.epicworld.presentation.theme.Black
import com.ruben.epicworld.presentation.theme.Gray300
import com.ruben.epicworld.presentation.theme.PinkA400
import com.ruben.epicworld.presentation.theme.Typography
import com.ruben.epicworld.presentation.theme.White
import com.ruben.epicworld.presentation.utility.showToast
import com.ruben.epicworld.presentation.videos.GameVideosViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Created by Ruben Quadros on 09/08/21
 **/
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
    val state = gameVideosViewModel.uiState().collectAsState()
    when (state.value.screenState) {
        ScreenState.Loading -> {
            LoadingView(modifier = Modifier.fillMaxSize())
        }
        ScreenState.Error -> {
            gameVideosViewModel.handleGameVideoError()
            navigateBack.invoke()
        }
        ScreenState.Success -> {
            state.value.gameVideos?.let {
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

@Composable
fun ShowGameVideos(gameVideos: GameVideosEntity) {
    Column {
        VideoPlayer(video = gameVideos.results[0].video)
        LazyColumn(
            content = {
            items(gameVideos.results) { trailer ->
                ShowTrailers(trailer)
            }
        })
    }
}

@Composable
fun ShowTrailers(trailer: VideoResultEntity) {
    ConstraintLayout(modifier = Modifier
        .padding(8.dp)
        .wrapContentSize()
        .clickable {

        }) {
        val (thumbnail, play, title) = createRefs()
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
        Image(
            contentScale = ContentScale.Crop,
            colorFilter = if (trailer.preview.isEmpty()) ColorFilter.tint(White) else ColorFilter.tint(PinkA400),
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
            text = trailer.name,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(thumbnail.top, margin = 8.dp)
                    start.linkTo(thumbnail.end, margin = 8.dp)
                    bottom.linkTo(thumbnail.bottom, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    width = Dimension.preferredWrapContent
                    height = Dimension.preferredWrapContent
            },
            color = Black,
            textAlign = TextAlign.Center,
            softWrap = true,
            style = Typography.h4
        )
        TrailerDivider()
    }
}

@Composable
fun TrailerDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 8.dp),
        color = Gray300
    )
}

@Composable
fun VideoPlayer(video: String) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(video)
            this.setMediaItem(mediaItem)
            this.prepare()
            this.playWhenReady = true
        }
    }

    DisposableEffect(AndroidView(modifier = Modifier.background(Black),
        factory = {
        PlayerView(context).apply {
            player = exoPlayer
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, config.screenHeightDp)
        }
    })) {
        onDispose {
            exoPlayer.release()
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
    ShowTrailers(trailer = VideoResultEntity("", "GTA Online: DeadLine GTA Online: DeadLine", ""))
}