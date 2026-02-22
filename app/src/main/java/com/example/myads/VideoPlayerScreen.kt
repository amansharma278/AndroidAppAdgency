package com.example.myads

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

@Composable
fun VideoPlayerScreen() {
    val context = LocalContext.current
    val apiService = remember { NetworkModule.provideApiService(context) }
    val deviceDetailsManager = remember { DeviceDetailsManager(context) }
    val viewModel: VideoViewModel = viewModel(factory = ViewModelFactory(apiService, deviceDetailsManager))
    val uiState by viewModel.uiState.collectAsState()

    // 1. Create and remember the PlayerView. It will be stable across all recompositions.
    val playerView = remember {
        PlayerView(context).apply {
            useController = false
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    // 2. This effect is keyed on the video URL. It will only re-run when the URL changes.
    DisposableEffect((uiState as? VideoUiState.Success)?.ad?.videoUrl) {
        val successState = uiState as? VideoUiState.Success
        val player: ExoPlayer?

        if (successState != null) {
            // Create and configure a new player for the new video.
            player = ExoPlayer.Builder(context).build().apply {
                val fullVideoUrl = "${NetworkModule.BASE_URL}/media/${successState.ad.videoUrl}"
                val mediaItem = MediaItem.fromUri(fullVideoUrl)

                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true

                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            viewModel.updatePlayingStatus(successState.adQueueId, "Completed")
                            viewModel.fetchNextAd()
                        }
                    }
                })
            }
            // Attach the new player to our stable PlayerView.
            playerView.player = player
        } else {
            // Not in a success state, so there is no player.
            player = null
        }

        onDispose {
            // When the effect is disposed (because the key changed or the screen is left),
            // release the player that was created in this effect run.
            player?.release()
        }
    }

    // Start fetching the first ad only when the component is first launched.
    LaunchedEffect(Unit) {
        viewModel.fetchNextAd()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 3. The PlayerView is always in the composition.
        AndroidView(factory = { playerView })

        // 4. Show loading/error overlays on top of the PlayerView.
        when (val state = uiState) {
            is VideoUiState.Loading -> {
                CircularProgressIndicator()
            }
            is VideoUiState.Error -> {
                Text(text = state.message)
            }
            is VideoUiState.Success -> {
                // The player is handled by the DisposableEffect above.
                // No action needed here.
            }
        }
    }
}
