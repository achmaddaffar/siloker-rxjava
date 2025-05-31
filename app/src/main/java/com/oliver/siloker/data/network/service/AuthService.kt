package com.oliver.siloker.data.network.service

import com.oliver.siloker.data.network.model.request.LoginRequestDto
import com.oliver.siloker.data.network.model.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<BaseResponse<String>>

    @Multipart
    @POST("auth/register")
    suspend fun register(
        @Part parts: List<MultipartBody.Part>
    ): Response<BaseResponse<Boolean>>
}