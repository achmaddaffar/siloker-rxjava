package com.oliver.siloker.data.network.service

import com.oliver.siloker.data.network.model.request.UpdateEmployerRequestDto
import com.oliver.siloker.data.network.model.request.UpdateJobSeekerRequestDto
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.model.response.GetProfileResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @GET("user")
    suspend fun getProfile(): Response<BaseResponse<GetProfileResponseDto>>

    @POST("user/register-job-seeker")
    suspend fun updateJobSeeker(
        @Body request: UpdateJobSeekerRequestDto
    ): Response<BaseResponse<Boolean>>

    @POST("user/register-employer")
    suspend fun updateEmployer(
        @Body request: UpdateEmployerRequestDto
    ): Response<BaseResponse<Boolean>>
}