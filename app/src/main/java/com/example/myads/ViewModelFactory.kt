package com.example.myads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val apiService: ApiService,
    private val deviceDetailsManager: DeviceDetailsManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoViewModel::class.java)) {
            return VideoViewModel(apiService, deviceDetailsManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
