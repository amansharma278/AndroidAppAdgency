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
                val exoPlayer = remember {
                    ExoPlayer.Builder(context).build()
                }

                var isPlayingReported by remember { mutableStateOf(false) }

                LaunchedEffect(state.ad.videoUrl) {
                    val fullVideoUrl = "${NetworkModule.BASE_URL}/media/${state.ad.videoUrl}"
                    val mediaItem = MediaItem.fromUri(fullVideoUrl)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true
                }

                DisposableEffect(Unit) {
                    val listener = object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            if (playbackState == Player.STATE_READY && !isPlayingReported) {
                                viewModel.updatePlayingStatus(state.ad.id, "Started")
                                isPlayingReported = true
                            }
                            if (playbackState == Player.STATE_ENDED) {
                                viewModel.updatePlayingStatus(state.ad.id, "Completed")
                            }
                        }
                    }
                    exoPlayer.addListener(listener)
                    onDispose {
                        exoPlayer.removeListener(listener)
                        exoPlayer.release()
                    }
                }

                LaunchedEffect(exoPlayer.isPlaying) {
                    if (exoPlayer.isPlaying) {
                        var aboutToCompleteReported by mutableStateOf(false)
                        while (true) {
                            delay(20000)
                            viewModel.updatePlayingStatus(state.ad.id, "Playing")

                            val remainingTime = exoPlayer.duration - exoPlayer.currentPosition
                            if (remainingTime <= 2000 && !aboutToCompleteReported) {
                                viewModel.updatePlayingStatus(state.ad.id, "About to Complete")
                                aboutToCompleteReported = true
                            }
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
                    modifier = Modifier.fillMaxSize()
                )
            }
            is VideoUiState.Error -> {
                Text(text = state.message)
            }
        }
    }
}
