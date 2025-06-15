package com.oliver.siloker.data.network.service

import com.oliver.siloker.data.network.model.request.LoginRequestDto
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.model.response.LoginResponseDto
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthService {

    @POST("auth/login")
    fun login(
        @Body request: LoginRequestDto
    ): Single<Response<BaseResponse<LoginResponseDto>>>

    @Multipart
    @POST("auth/register")
    fun register(
        @Part parts: List<MultipartBody.Part>
    ): Single<Response<BaseResponse<Boolean>>>
}