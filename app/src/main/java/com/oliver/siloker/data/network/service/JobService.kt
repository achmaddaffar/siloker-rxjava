package com.oliver.siloker.data.network.service

import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.model.response.JobAdResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface JobService {

    @GET("job")
    suspend fun getJobs(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<BaseResponse<JobAdResponseDto>>

    @Multipart
    @POST("job/create")
    suspend fun postJob(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<BaseResponse<Boolean>>
}