package com.oliver.siloker.data.network.service

import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.model.response.JobAdResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JobService {

    @GET("job")
    suspend fun getJobs(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<BaseResponse<JobAdResponseDto>>
}