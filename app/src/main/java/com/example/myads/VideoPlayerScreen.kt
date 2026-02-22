package com.example.myads

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun VideoPlayerScreen() {
    val context = LocalContext.current
    val apiService = remember { NetworkModule.provideApiService(context) }
    val deviceDetailsManager = remember { DeviceDetailsManager(context) }
    val viewModel: VideoViewModel = viewModel(factory = ViewModelFactory(apiService, deviceDetailsManager))
    val uiState by viewModel.uiState.collectAsState()

    val playerView = remember {
        PlayerView(context).apply {
            useController = false
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    DisposableEffect((uiState as? VideoUiState.Success)?.ad?.videoUrl) {
        val successState = uiState as? VideoUiState.Success
        val player: ExoPlayer?

        if (successState != null) {
            var isPlayingReported = false
            player = ExoPlayer.Builder(context).build().apply {
                val fullVideoUrl = "${NetworkModule.BASE_URL}/media/${successState.ad.videoUrl}"
                val mediaItem = MediaItem.fromUri(fullVideoUrl)

                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true

                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_READY && !isPlayingReported) {
                            Log.d("VideoPlayerScreen", "Attempting to report 'Started' status for adQueueId: ${successState.adQueueId}")
                            viewModel.updatePlayingStatus(successState.adQueueId, "Started")
                            isPlayingReported = true
                        }
                        if (playbackState == Player.STATE_ENDED) {
                            Log.d("VideoPlayerScreen", "Attempting to report 'Completed' status for adQueueId: ${successState.adQueueId}")
                            viewModel.updatePlayingStatus(successState.adQueueId, "Completed")
                            viewModel.fetchNextAd()
                        }
                    }
                })
            }
            playerView.player = player
        } else {
            player = null
        }

        onDispose {
            playerView.player = null
            player?.release()
        }
    }

    LaunchedEffect((uiState as? VideoUiState.Success)?.ad?.videoUrl) {
        val successState = uiState as? VideoUiState.Success
        if (successState != null) {
            var aboutToCompleteReported = false
            while (isActive) {
                val player = playerView.player
                if (player?.isPlaying == true) {
                    delay(5000) // Every 5 seconds
                    Log.d("VideoPlayerScreen", "Attempting to report 'Playing' status for adQueueId: ${successState.adQueueId}")
                    viewModel.updatePlayingStatus(successState.adQueueId, "Playing")

                    val remainingTime = player.duration - player.currentPosition
                    if (remainingTime <= 2000 && !aboutToCompleteReported) {
                        Log.d("VideoPlayerScreen", "Attempting to report 'About to Complete' status for adQueueId: ${successState.adQueueId}")
                        viewModel.updatePlayingStatus(successState.adQueueId, "About to Complete")
                        aboutToCompleteReported = true
                    } else if (remainingTime > 2000 && aboutToCompleteReported) {
                        aboutToCompleteReported = false
                    }
                } else {
                    delay(1000)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchNextAd()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(factory = { playerView })

        when (uiState) {
            is VideoUiState.Loading -> {
                CircularProgressIndicator()
            }
            is VideoUiState.Error -> {
                val errorState = uiState as VideoUiState.Error
                Text(text = errorState.message)
            }
            is VideoUiState.Success -> {
                // Player is handled in the DisposableEffect
            }
        }
    }
}
