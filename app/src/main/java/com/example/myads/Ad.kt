package com.example.myads

import com.google.gson.annotations.SerializedName

data class Ad(
    val id: Int,
    val title: String,
    val description: String,
    val duration: Int,
    @SerializedName("play_limit") val playLimit: Int,
    val priority: Int,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("created_at") val createdAt: String,
    val video: String,
    @SerializedName("created_by") val createdBy: Int
)
