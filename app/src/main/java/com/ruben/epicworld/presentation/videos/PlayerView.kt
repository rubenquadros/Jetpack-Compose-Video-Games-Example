package com.ruben.epicworld.presentation.videos

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.ruben.epicworld.domain.entity.gamevideos.PlayerWrapper
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import com.ruben.epicworld.presentation.utility.Constants
import com.ruben.epicworld.presentation.utility.noRippleClickable
import com.ruben.epicworld.presentation.utility.setPortrait
import kotlinx.coroutines.delay

/**
 * Created by Ruben Quadros on 02/07/22
 **/
@Composable
fun PlayerView(
    modifier: Modifier = Modifier,
    playerWrapper: PlayerWrapper,
    isFullScreen: Boolean,
    onTrailerChange: ((Int) -> Unit)? = null,
    onFullScreenToggle: (isFullScreen: Boolean) -> Unit,
    navigateBack: (() -> Unit)? = null
) {
    val context = LocalContext.current

    BackHandler {
        if (isFullScreen) {
            context.setPortrait()
            onFullScreenToggle.invoke(false)
        } else {
            navigateBack?.invoke()
        }
    }

    Box(modifier = modifier) {

        var shouldShowControls by remember {
            mutableStateOf(false)
        }

        var isPlaying by remember {
            mutableStateOf(true)
        }

        var isVisible by remember {
            mutableStateOf(true)
        }

        var title by remember {
            mutableStateOf(playerWrapper.exoPlayer.currentMediaItem?.mediaMetadata?.displayTitle.toString())
        }

        var videoTimer by remember {
            mutableStateOf(0L)
        }

        var totalDuration by remember {
            mutableStateOf(0L)
        }

        LaunchedEffect(key1 = shouldShowControls) {
            if (shouldShowControls) {
                delay(Constants.PLAYER_CONTROLS_VISIBILITY)
                shouldShowControls = false
            }
        }



        DisposableEffect(key1 = true) {
            val listener = object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    totalDuration = player.duration
                    videoTimer = player.contentPosition
                    if (player.contentPosition >= 200) isVisible = false
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    onTrailerChange?.invoke(playerWrapper.exoPlayer.currentPeriodIndex)
                    isVisible = true
                    title = mediaItem?.mediaMetadata?.displayTitle.toString()
                }
            }

            playerWrapper.exoPlayer.addListener(listener)

            onDispose {
                playerWrapper.exoPlayer.removeListener(listener)
            }
        }

        VideoPlayer(
            modifier = Modifier.fillMaxSize(),
            playerWrapper = playerWrapper,
            onPlayerClick = {
                shouldShowControls = shouldShowControls.not()
            }
        )

        PlayerControls(
            modifier = Modifier.fillMaxSize(),
            isVisible = { shouldShowControls },
            isPlaying = { isPlaying },
            totalDuration = { totalDuration },
            isFullScreen = isFullScreen,
            onPrevious = { playerWrapper.exoPlayer.seekToPrevious() },
            onNext = { playerWrapper.exoPlayer.seekToNext() },
            onReplay = { playerWrapper.exoPlayer.seekBack() },
            onForward = { playerWrapper.exoPlayer.seekForward() },
            onPauseToggle = {
                if (playerWrapper.exoPlayer.isPlaying) {
                    playerWrapper.exoPlayer.pause()
                } else {
                    playerWrapper.exoPlayer.play()
                }
                isPlaying = isPlaying.not()
            },
            onSeekChanged = { position -> playerWrapper.exoPlayer.seekTo(position.toLong()) },
            videoTimer = { videoTimer },
            onFullScreenToggle = onFullScreenToggle
        )

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(animationSpec = tween(durationMillis = 250)),
            modifier = Modifier
                .zIndex(2f)
                .align(Alignment.TopStart)
        ) {
            Text(
                text = title,
                style = EpicWorldTheme.typography.subTitle2,
                color = EpicWorldTheme.colors.onBackground,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
}

@Composable
private fun VideoPlayer(
    modifier: Modifier = Modifier,
    playerWrapper: PlayerWrapper,
    onPlayerClick: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .background(EpicWorldTheme.colors.background)
            .noRippleClickable {
                onPlayerClick.invoke()
            }
    ) {
        AndroidView(
            modifier = modifier
                .testTag("VideoPlayer"),
            factory = {
                StyledPlayerView(context).apply {
                    player = playerWrapper.exoPlayer
                    useController = false
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            })
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPlayerView() {
    val context = LocalContext.current
    PlayerView(
        modifier = Modifier.fillMaxSize(),
        playerWrapper = PlayerWrapper(exoPlayer = ExoPlayer.Builder(context).build()),
        isFullScreen = false,
        onFullScreenToggle = {}
    )
}