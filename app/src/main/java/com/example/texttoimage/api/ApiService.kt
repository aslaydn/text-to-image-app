package com.example.texttoimage.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import okhttp3.RequestBody

interface ApiService {
    @POST("models/stabilityai/stable-diffusion-xl-base-1.0")
    suspend fun generateImage(
        @Header("Authorization") token: String,
        @Body request: RequestBody
    ): Response<ResponseBody>
}