package com.example.myads

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class TokenResponse(
    val access: String,
    val refresh: String
)

data class AdQueueRequest(
    val device: Int,
    val ad: Int,
    val status: String
)

data class AdQueueResponse(
    val id: Int,
    val device: Int,
    val ad: Int,
    val status: String
)

data class UpdatePlayingStatusRequest(
    val id: Int,
    val status: String
)

interface ApiService {
    @POST("/api/token/")
    suspend fun login(@Body credentials: Map<String, String>): Response<TokenResponse>

    @POST("/api/token/refresh/")
    suspend fun refreshToken(@Body refreshToken: Map<String, String>): Response<TokenResponse>

    @GET("/api/device/me/")
    suspend fun getDeviceDetails(): Response<DeviceDetails>

    @GET("/api/device/{deviceId}/next-ad/")
    suspend fun getNextAd(@Path("deviceId") deviceId: Int): Response<Ad>

    @POST("/api/device/{deviceId}/add-in-queue/")
    suspend fun addAdToQueue(@Path("deviceId") deviceId: Int, @Body adQueueRequest: AdQueueRequest): Response<AdQueueResponse>

    @POST("/api/device/{deviceId}/update-playing-status/")
    suspend fun updatePlayingStatus(@Path("deviceId") deviceId: Int, @Body updatePlayingStatusRequest: UpdatePlayingStatusRequest): Response<Unit>
}
