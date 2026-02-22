package com.example.myads

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class VideoUiState {
    object Loading : VideoUiState()
    data class Success(val ad: Ad, val adQueueId: Int) : VideoUiState()
    data class Error(val message: String) : VideoUiState()
}

class VideoViewModel(
    private val apiService: ApiService,
    private val deviceDetailsManager: DeviceDetailsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<VideoUiState>(VideoUiState.Loading)
    val uiState: StateFlow<VideoUiState> = _uiState

    fun fetchNextAd() {
        viewModelScope.launch {
            _uiState.value = VideoUiState.Loading
            val deviceId = withContext(Dispatchers.IO) {
                deviceDetailsManager.getDeviceId()
            } ?: return@launch
            try {
                val response = apiService.getNextAd(deviceId)
                if (response.isSuccessful && response.body() != null) {
                    val ad = response.body()!!
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
            val deviceId = withContext(Dispatchers.IO) {
                deviceDetailsManager.getDeviceId()
            } ?: return@launch
            try {
                val adQueueRequest = AdQueueRequest(device = deviceId, ad = ad.id, status = "In-Queue")
                val response = apiService.addAdToQueue(deviceId, adQueueRequest)
                if (response.isSuccessful && response.body() != null) {
                    val adQueueId = response.body()!!.id
                    _uiState.value = VideoUiState.Success(ad, adQueueId)
                } else {
                    _uiState.value = VideoUiState.Error("Failed to add ad to queue")
                }
            } catch (e: Exception) {
                 _uiState.value = VideoUiState.Error("An error occurred: ${e.message}")
            }
        }
    }

    fun updatePlayingStatus(adQueueId: Int, status: String) {
        viewModelScope.launch {
            val deviceId = try {
                withContext(Dispatchers.IO) {
                    deviceDetailsManager.getDeviceId()
                }
            } catch (e: Exception) {
                Log.e("VideoViewModel", "Failed to get deviceId", e)
                null
            } ?: return@launch

            try {
                Log.d("VideoViewModel", "Updating status for adQueueId: $adQueueId to '$status'")
                val response = apiService.updatePlayingStatus(deviceId, UpdatePlayingStatusRequest(id = adQueueId, status = status))
                if (!response.isSuccessful) {
                    Log.e("VideoViewModel", "Failed to update status. Code: ${response.code()}, Message: ${response.message()}, Body: ${response.errorBody()?.string()}")
                } else {
                    Log.d("VideoViewModel", "Successfully updated status for adQueueId: $adQueueId to '$status'")
                }
            } catch (e: Exception) {
                Log.e("VideoViewModel", "Exception while updating status for adQueueId: $adQueueId to '$status'", e)
            }
        }
    }
}
