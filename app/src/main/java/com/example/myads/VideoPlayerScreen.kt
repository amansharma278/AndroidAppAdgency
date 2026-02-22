package com.example.myads

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    LaunchedEffect(Unit) {
        viewModel.fetchNextAd()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is VideoUiState.Loading -> {
                CircularProgressIndicator()
            }
            is VideoUiState.Success -> {
                val exoPlayer = remember(state.ad.videoUrl) {
                    ExoPlayer.Builder(context).build()
                }

                DisposableEffect(exoPlayer) {
                    onDispose {
                        exoPlayer.release()
                    }
                }

                var isPlayingReported by remember(exoPlayer) { mutableStateOf(false) }
                var aboutToCompleteReported by remember(exoPlayer) { mutableStateOf(false) }

                LaunchedEffect(exoPlayer, state.ad.videoUrl) {
                    val fullVideoUrl = "${NetworkModule.BASE_URL}/media/${state.ad.videoUrl}"
                    val mediaItem = MediaItem.fromUri(fullVideoUrl)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true
                }

                DisposableEffect(exoPlayer, state.adQueueId) {
                    val listener = object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            if (playbackState == Player.STATE_READY && !isPlayingReported) {
                                viewModel.updatePlayingStatus(state.adQueueId, "Started")
                                isPlayingReported = true
                            }
                            if (playbackState == Player.STATE_ENDED) {
                                viewModel.updatePlayingStatus(state.adQueueId, "Completed")
                                viewModel.fetchNextAd()
                            }
                        }
                    }
                    exoPlayer.addListener(listener)
                    onDispose {
                        exoPlayer.removeListener(listener)
                    }
                }

                LaunchedEffect(exoPlayer, state.adQueueId) {
                    while (isActive) {
                        if (exoPlayer.isPlaying) {
                            delay(5000)
                            viewModel.updatePlayingStatus(state.adQueueId, "Playing")

                            val remainingTime = exoPlayer.duration - exoPlayer.currentPosition
                            if (remainingTime <= 2000 && !aboutToCompleteReported) {
                                viewModel.updatePlayingStatus(state.adQueueId, "About to Complete")
                                aboutToCompleteReported = true
                            } else if (remainingTime > 2000 && aboutToCompleteReported) {
                                aboutToCompleteReported = false
                            }
                        } else {
                            delay(1000)
                        }
                    }
                }

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
                    update = { view ->
                        view.player = exoPlayer
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is VideoUiState.Error -> {
                Text(text = state.message)
            }
        }
    }
}
