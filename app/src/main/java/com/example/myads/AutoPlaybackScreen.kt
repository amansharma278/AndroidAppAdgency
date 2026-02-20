package com.example.myads

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.myads.ui.theme.MyAdsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(UnstableApi::class)
@Composable
fun AutoPlaybackScreen(ad: Ad? = null, onAdminGesture: () -> Unit = {}) {
    val context = LocalContext.current
    val currentAd by rememberUpdatedState(ad)

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL

            addListener(object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT) {
                        Log.d("AutoPlaybackScreen", "Video Finished (Looping)")
                        performDummyApiCall(currentAd?.id, "FINISHED_AND_REPEATING")
                    }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_ENDED -> {
                            Log.d("AutoPlaybackScreen", "Playback State: ENDED")
                            performDummyApiCall(currentAd?.id, "FINISHED")
                        }
                        Player.STATE_READY -> {
                            Log.d("AutoPlaybackScreen", "Playback State: READY. Duration: ${duration}ms")
                        }
                    }
                }
            })
        }
    }

    LaunchedEffect(currentAd) {
        currentAd?.video?.let { url ->
            Log.d("AutoPlaybackScreen", "Loading Video: $url")
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        } ?: run {
            exoPlayer.stop()
            exoPlayer.clearMediaItems()
        }
    }

    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(exoPlayer, currentAd) {
        var notifiedAboutToEnd = false
        while (isActive) {
            if (exoPlayer.playbackState == Player.STATE_READY) {
                val duration = exoPlayer.duration
                val position = exoPlayer.currentPosition

                if (duration > 0) {
                    progress = position.toFloat() / duration

                    val remainingTime = duration - position
                    // About to end: within last 2 seconds
                    if (remainingTime <= 2000 && !notifiedAboutToEnd) {
                        Log.d("AutoPlaybackScreen", "About to end: ${remainingTime}ms remaining")
                        performDummyApiCall(currentAd?.id, "ABOUT_TO_END")
                        notifiedAboutToEnd = true
                    }

                    // Reset flag when it loops back to start (position is near 0)
                    if (position < 1000 && notifiedAboutToEnd) {
                        notifiedAboutToEnd = false
                    }
                }
            }
            delay(500) // Increased frequency for better precision
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (ad == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Video Selected",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White.copy(alpha = 0.3f)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.TopStart)
                .clickable(onClick = onAdminGesture)
        )

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.BottomCenter),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color.Transparent
        )
    }
}

private fun performDummyApiCall(adId: Int?, event: String) {
    Log.d("AutoPlaybackScreen", ">>>> API CALL [Ad ID: $adId] | Event: $event")
}

@Preview(showBackground = true, device = "spec:width=1920dp,height=1080dp,navigation=buttons")
@Composable
fun AutoPlaybackScreenPreview() {
    MyAdsTheme {
        AutoPlaybackScreen(ad = Ad(1, "Preview", "Description", 30, 10, 1, "2026-02-01T00:00:00Z", "2026-03-01T00:00:00Z", true, "2026-02-19T14:55:04.195875Z", "https://www.w3schools.com/tags/mov_bbb.mp4", 1))
    }
}
