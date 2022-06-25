package com.ruben.epicworld.presentation.videos

import android.content.Context
import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.ruben.epicworld.domain.entity.gamevideos.VideoResultEntity
import com.ruben.epicworld.presentation.utility.findActivity

/**
 * Created by Ruben Quadros on 25/06/22
 **/
class PlayerViewManager {

    private var playerView: View? = null
    private var player: Player? = null
    private var mediaListener: MediaListener? = null

    /**
     *
     */
    fun getPlayerView(
        context: Context,
        videos: List<VideoResultEntity>,
        //mediaListener: MediaListener
    ): View {
        this.mediaListener = null
        //this.mediaListener = mediaListener
        return playerView ?: createPlayerView(
            context = context,
            videos = videos
        )
    }

    /**
     *
     */
    fun playFromPlaylist(position: Int) {
        player?.seekTo(position, C.TIME_UNSET)
        player?.playWhenReady = true
    }

    /**
     *
     */
    fun pauseVideo() {
        player?.pause()
    }

    /**
     *
     */
    fun playVideo() {
        if (player?.isPlaying?.not() == true) {
            player?.play()
        }
    }

    /**
     *
     */
    fun release() {
        player?.release()
        player = null
        playerView = null
        mediaListener = null
    }

    private fun createPlayerView(
        context: Context,
        videos: List<VideoResultEntity>
    ): View {
        playerView = StyledPlayerView(context).apply {
            player = getPlayer(context = context, videos = videos)
            setFullscreenButtonClickListener { isFullScreen ->
                context.findActivity()?.requestedOrientation = if (isFullScreen) {
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return playerView!!
    }

    private fun getPlayer(context: Context, videos: List<VideoResultEntity>): Player {
        fun createMediaItems(): List<MediaItem> {
            return videos.map {
                MediaItem.Builder().setUri(it.video).setMediaId(it.id.toString()).setTag(it)
                    .setMediaMetadata(MediaMetadata.Builder().setDisplayTitle(it.name).build())
                    .build()
            }
        }

        return ExoPlayer.Builder(context).build().apply {
            this.setMediaItems(createMediaItems())
            this.prepare()
            this.addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    mediaListener?.currentPosition(position = player.currentPosition)
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    mediaListener?.onItemTransition(
                        newTitle = mediaItem?.mediaMetadata?.displayTitle.toString(),
                        newPosition = currentPeriodIndex
                    )
                }
            })
            this.playWhenReady = true
        }
    }
}

interface MediaListener {
    fun currentPosition(position: Long)
    fun onItemTransition(newTitle: String, newPosition: Int)
}