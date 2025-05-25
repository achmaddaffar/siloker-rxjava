package com.oliver.siloker.data.network.service

import com.oliver.siloker.data.network.model.request.LoginRequestDto
import com.oliver.siloker.data.network.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<BaseResponse<String>>
}