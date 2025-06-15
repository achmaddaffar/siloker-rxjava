package com.oliver.siloker.data.network.service

import com.oliver.siloker.data.network.model.request.UpdateEmployerRequestDto
import com.oliver.siloker.data.network.model.request.UpdateJobSeekerRequestDto
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.model.response.GetProfileResponseDto
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserService {

    @GET("user")
    fun getProfile(): Single<Response<BaseResponse<GetProfileResponseDto>>>

    @Multipart
    @POST("user/update/profile_picture")
    fun updateProfilePicture(
        @Part part: MultipartBody.Part
    ): Single<Response<BaseResponse<Boolean>>>

    @POST("user/register-job-seeker")
    fun updateJobSeeker(
        @Body request: UpdateJobSeekerRequestDto
    ): Single<Response<BaseResponse<Boolean>>>

    @POST("user/register-employer")
    fun updateEmployer(
        @Body request: UpdateEmployerRequestDto
    ): Single<Response<BaseResponse<Boolean>>>
}