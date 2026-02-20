package com.example.myads

import com.google.gson.annotations.SerializedName

data class DeviceDetails(
    val id: Int,
    @SerializedName("device_name") val deviceName: String,
    @SerializedName("device_id") val deviceId: String,
    val location: String,
    @SerializedName("is_online") val isOnline: Boolean,
    @SerializedName("last_active") val lastActive: String?,
    @SerializedName("assigned_ads") val assignedAds: List<Int>
)
