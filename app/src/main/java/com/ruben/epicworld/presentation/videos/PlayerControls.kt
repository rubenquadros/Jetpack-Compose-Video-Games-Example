package com.ruben.epicworld.presentation.videos

import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.ruben.epicworld.R
import com.ruben.epicworld.presentation.theme.EpicWorldTheme
import com.ruben.epicworld.presentation.utility.findActivity
import com.ruben.epicworld.presentation.utility.formatMinSec

/**
 * Created by Ruben Quadros on 02/07/22
 **/
@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    isPlaying: () -> Boolean,
    videoTimer: () -> Long,
    totalDuration: () -> Long,
    isFullScreen: Boolean,
    onPauseToggle: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onReplay: () -> Unit,
    onForward: () -> Unit,
    onSeekChanged: (newValue: Float) -> Unit
) {

    val visible = remember(isVisible()) {
        isVisible()
    }

    val playing = remember(isPlaying()) {
        isPlaying()
    }

    val duration = remember(totalDuration()) {
        totalDuration().coerceAtLeast(0)
    }

    val timer = remember(videoTimer()) {
        videoTimer()
    }

    val context = LocalContext.current

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        ConstraintLayout(
            constraintSet = ConstraintSet {
                val centerControls = createRefFor("center_controls")
                val seek = createRefFor("seek")
                val timing = createRefFor("timing")
                val fullScreenToggle = createRefFor("full_screen_toggle")

                constrain(centerControls) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }

                constrain(seek) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 32.dp)
                    width = Dimension.fillToConstraints
                }

                constrain(timing) {
                    top.linkTo(seek.bottom)
                    start.linkTo(parent.start, margin = 16.dp)
                }

                constrain(fullScreenToggle) {
                    top.linkTo(seek.bottom)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.value(24.dp)
                    height = Dimension.value(24.dp)
                }
            }
        ) {

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
                modifier = Modifier.layoutId("center_controls"),
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
                            if (playing) {
                                com.google.android.exoplayer2.ui.R.drawable.exo_ic_pause_circle_filled
                            } else {
                                com.google.android.exoplayer2.ui.R.drawable.exo_ic_play_circle_filled
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

            Box(modifier = Modifier.layoutId("seek")) {
                Slider(
                    modifier = Modifier.padding(vertical = 4.dp),
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

            Text(
                modifier = Modifier.layoutId("timing"),
                text = duration.formatMinSec(),
                color = EpicWorldTheme.colors.onBackground,
                style = EpicWorldTheme.typography.subTitle2
            )

            IconButton(
                modifier = Modifier.layoutId("full_screen_toggle"),
                onClick = {
                    context.findActivity()?.requestedOrientation = if (isFullScreen.not()) {
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    } else {
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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

@Preview(showBackground = true)
@Composable
private fun PreviewPlayerControls() {
    PlayerControls(
        modifier = Modifier.fillMaxSize(),
        isVisible = { true },
        isPlaying = { true },
        videoTimer = { 0L },
        totalDuration = { 0 },
        isFullScreen = false,
        onForward = {},
        onNext = {},
        onPauseToggle = {},
        onPrevious = {},
        onReplay = {},
        onSeekChanged = {}
    )
}