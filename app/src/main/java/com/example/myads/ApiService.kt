
package com.example.myads

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class TokenResponse(
    val access: String,
    val refresh: String
)

data class AdQueueRequest(
    val device: Int,
    val ad: Int,
    val status: String
)

interface ApiService {
    @POST("/api/token/")
    suspend fun login(@Body credentials: Map<String, String>): Response<TokenResponse>

    @POST("/api/token/refresh/")
    suspend fun refreshToken(@Body refreshToken: Map<String, String>): Response<TokenResponse>

    @GET("/api/device/1/next-ad/")
    suspend fun getNextAd(): Response<Ad>

    @POST("/api/device/1/add-in-queue/")
    suspend fun addAdToQueue(@Body adQueueRequest: AdQueueRequest): Response<Unit>
}
