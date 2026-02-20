package com.example.myads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class VideoUiState {
    object Loading : VideoUiState()
    data class Success(val ad: Ad) : VideoUiState()
    data class Error(val message: String) : VideoUiState()
}

class VideoViewModel(private val apiService: ApiService) : ViewModel() {

    private val _uiState = MutableStateFlow<VideoUiState>(VideoUiState.Loading)
    val uiState: StateFlow<VideoUiState> = _uiState

    private var statusUpdateJob: Job? = null

    fun fetchNextAd() {
        viewModelScope.launch {
            _uiState.value = VideoUiState.Loading
            try {
                val response = apiService.getNextAd()
                if (response.isSuccessful && response.body() != null) {
                    val ad = response.body()!!
                    _uiState.value = VideoUiState.Success(ad)
                    addAdToQueue(ad)
                } else {
                    _uiState.value = VideoUiState.Error("Failed to fetch next ad")
                }
            } catch (e: Exception) {
                _uiState.value = VideoUiState.Error("An error occurred: ${e.message}")
            }
        }
    }

    private fun addAdToQueue(ad: Ad) {
        viewModelScope.launch {
            try {
                apiService.addAdToQueue(AdQueueRequest(1, ad.id, "In-Queue"))
                updatePlayingStatus(ad.id, "Started")
                startStatusUpdates(ad.id)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun updatePlayingStatus(adId: Int, status: String) {
        viewModelScope.launch {
            try {
                apiService.updatePlayingStatus(UpdatePlayingStatusRequest(adId, status))
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun startStatusUpdates(adId: Int) {
        statusUpdateJob?.cancel()
        statusUpdateJob = viewModelScope.launch {
            while (true) {
                delay(20000)
                updatePlayingStatus(adId, "Playing")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        statusUpdateJob?.cancel()
    }
}
