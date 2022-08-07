package com.ruben.epicworld.presentation.videos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.exoplayer2.Player.STATE_ENDED
import com.ruben.epicworld.R
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import com.ruben.epicworld.presentation.utility.formatMinSec
import com.ruben.epicworld.presentation.utility.setLandscape
import com.ruben.epicworld.presentation.utility.setPortrait

/**
 * Created by Ruben Quadros on 02/07/22
 **/
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    isPlaying: () -> Boolean,
    videoTimer: () -> Long,
    bufferedPercentage: () -> Int,
    playbackState: () -> Int,
    getTitle: () -> String,
    totalDuration: () -> Long,
    isFullScreen: Boolean,
    onPauseToggle: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onReplay: () -> Unit,
    onForward: () -> Unit,
    onSeekChanged: (newValue: Float) -> Unit,
    onFullScreenToggle: (isFullScreen: Boolean) -> Unit
) {

    val visible = remember(isVisible()) { isVisible() }

    val playing = remember(isPlaying()) { isPlaying() }

    val duration = remember(totalDuration()) { totalDuration().coerceAtLeast(0) }

    val timer = remember(videoTimer()) { videoTimer() }

    val title = remember(getTitle()) { getTitle() }

    val buffer = remember(bufferedPercentage()) { bufferedPercentage() }

    val playerState = remember(playbackState()) {
        playbackState()
    }

    val context = LocalContext.current

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .testTag("PlayerControlsParent")
                .background(EpicWorldTheme.colors.background.copy(alpha = 0.6f))
        ) {

            Text(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .testTag("VideoTitle")
                    .animateEnterExit(
                        enter = slideInVertically(
                            initialOffsetY = { fullHeight: Int -> -fullHeight }
                        ),
                        exit = shrinkVertically()
                    ),
                text = title,
                style = EpicWorldTheme.typography.subTitle2,
                color = EpicWorldTheme.colors.onBackground
            )

            val controlButtonModifier: Modifier = remember(isFullScreen) {
                if (isFullScreen) {
                    Modifier
                        .padding(horizontal = 8.dp)
                        .size(40.dp)
                } else {
                    Modifier.size(32.dp)
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .testTag("VideoControlParent"),
                horizontalArrangement = if (isFullScreen) {
                    Arrangement.Center
                } else {
                    Arrangement.SpaceEvenly
                }
            ) {
                IconButton(
                    modifier = controlButtonModifier,
                    onClick = onPrevious
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = com.google.android.exoplayer2.ui.R.drawable.exo_ic_skip_previous),
                        contentDescription = stringResource(id = R.string.play_previous)
                    )
                }

                IconButton(
                    modifier = controlButtonModifier,
                    onClick = onReplay
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = R.drawable.ic_replay_5),
                        contentDescription = stringResource(id = R.string.rewind_5)
                    )
                }

                IconButton(
                    modifier = controlButtonModifier,
                    onClick = onPauseToggle
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(
                            id =
                            when {
                                playing -> {
                                    com.google.android.exoplayer2.ui.R.drawable.exo_ic_pause_circle_filled
                                }
                                playing.not() && playerState == STATE_ENDED -> {
                                    R.drawable.ic_replay
                                }
                                else -> {
                                    com.google.android.exoplayer2.ui.R.drawable.exo_ic_play_circle_filled
                                }
                            }
                        ),
                        contentDescription = stringResource(id = R.string.toggle_play)
                    )
                }

                IconButton(
                    modifier = controlButtonModifier,
                    onClick = onForward
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = R.drawable.ic_forward_10),
                        contentDescription = stringResource(id = R.string.forward_10)
                    )
                }

                IconButton(
                    modifier = controlButtonModifier,
                    onClick = onNext
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = com.google.android.exoplayer2.ui.R.drawable.exo_ic_skip_next),//R.drawable.ic_skip_next
                        contentDescription = stringResource(id = R.string.play_next)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = if (isFullScreen) 32.dp else 16.dp)
                    .testTag("VideoSeek")
                    .animateEnterExit(
                        enter = slideInVertically(
                            initialOffsetY = { fullHeight: Int -> fullHeight }
                        ),
                        exit = slideOutVertically(
                            targetOffsetY = { fullHeight: Int -> fullHeight }
                        )
                    )
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Slider(
                        value = buffer.toFloat(),
                        enabled = false,
                        onValueChange = { /*do nothing*/ },
                        valueRange = 0f..100f,
                        colors =
                        SliderDefaults.colors(
                            disabledThumbColor = Color.Transparent,
                            disabledActiveTrackColor = EpicWorldTheme.colors.onDisabled
                        )
                    )

                    Slider(
                        value = timer.toFloat(),
                        onValueChange = {
                            onSeekChanged.invoke(it)
                        },
                        valueRange = 0f..duration.toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = EpicWorldTheme.colors.onBackground,
                            activeTrackColor = EpicWorldTheme.colors.onBackground
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .testTag("VideoTime")
                            .padding(start = 16.dp)
                            .animateEnterExit(
                                enter = slideInVertically(
                                    initialOffsetY = { fullHeight: Int -> fullHeight }
                                ),
                                exit = slideOutVertically(
                                    targetOffsetY = { fullHeight: Int -> fullHeight }
                                )
                            ),
                        text = duration.formatMinSec(),
                        color = EpicWorldTheme.colors.onBackground,
                        style = EpicWorldTheme.typography.subTitle2
                    )

                    IconButton(
                        modifier = Modifier
                            .testTag("FullScreenToggleButton")
                            .padding(end = 16.dp)
                            .size(24.dp)
                            .animateEnterExit(
                                enter = slideInVertically(
                                    initialOffsetY = { fullHeight: Int -> fullHeight }
                                ),
                                exit = slideOutVertically(
                                    targetOffsetY = { fullHeight: Int -> fullHeight }
                                )
                            ),
                        onClick = {
                            if (isFullScreen.not()) {
                                context.setLandscape()
                            } else {
                                context.setPortrait()
                            }.also {
                                onFullScreenToggle.invoke(isFullScreen.not())
                            }
                        }
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            painter = painterResource(
                                id = if (isFullScreen) {
                                    com.google.android.exoplayer2.ui.R.drawable.exo_controls_fullscreen_exit
                                } else {
                                    com.google.android.exoplayer2.ui.R.drawable.exo_controls_fullscreen_enter
                                }
                            ),
                            contentDescription = stringResource(id = R.string.toggle_full_screen)
                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PreviewPlayerControls() {
    PlayerControls(
        modifier = Modifier.fillMaxSize(),
        isVisible = { true },
        isPlaying = { true },
        videoTimer = { 0L },
        totalDuration = { 0 },
        bufferedPercentage = { 50 },
        isFullScreen = false,
        onForward = {},
        onNext = {},
        onPauseToggle = {},
        onPrevious = {},
        onReplay = {},
        onSeekChanged = {},
        onFullScreenToggle = {},
        getTitle = { "" },
        playbackState = { 1 }
    )
}