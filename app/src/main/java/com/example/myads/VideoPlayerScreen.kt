package com.example.myads

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
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun VideoPlayerScreen() {
    val context = LocalContext.current
    val apiService = remember { NetworkModule.provideApiService(context) }
    val viewModel: VideoViewModel = viewModel(factory = ViewModelFactory(apiService))
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
                // TODO: Implement video player
                Text(text = "Playing ad: ${state.ad.videoUrl}")
            }
            is VideoUiState.Error -> {
                Text(text = state.message)
            }
        }
    }
}
