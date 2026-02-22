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
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

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

                LaunchedEffect(state.ad.video) {
                    val BASE_URL = "http://192.168.1.7:8000"
                    val mediaItem = MediaItem.fromUri("${BASE_URL}/media/${state.ad.video}")
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true
                }

                DisposableEffect(Unit) {
                    onDispose {
                        exoPlayer.release()
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
