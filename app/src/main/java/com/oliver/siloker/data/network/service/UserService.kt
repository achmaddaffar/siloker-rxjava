package com.oliver.siloker.data.network.service

import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.model.response.GetProfileResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface UserService {

    @GET("user")
    suspend fun getProfile(): Response<BaseResponse<GetProfileResponseDto>>
}