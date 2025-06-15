package com.oliver.siloker.data.network.service

import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.model.response.GetApplicantsResponseDto
import com.oliver.siloker.data.network.model.response.GetApplicantsResponseItemDto
import com.oliver.siloker.data.network.model.response.JobAdResponseDto
import com.oliver.siloker.data.network.model.response.JobDetailResponseDto
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface JobService {

    @GET("job")
    suspend fun getJobs(
        @Query("query") query: String,
        @Query("employer_id") employerId: Long?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<BaseResponse<JobAdResponseDto>>

    @GET("job/{job_id}")
    fun getJobDetail(
        @Path("job_id") jobId: Long
    ): Single<Response<BaseResponse<JobDetailResponseDto>>>

    @Multipart
    @POST("job/create")
    suspend fun postJob(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<BaseResponse<Boolean>>

    @Multipart
    @POST("job/apply")
    fun applyJob(
        @Part("job_id") jobId: RequestBody,
        @Part cv: MultipartBody.Part
    ): Single<Response<BaseResponse<Boolean>>>

    @GET("job/applicants")
    suspend fun getApplicants(
        @Query("job_id") jobId: Long?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<BaseResponse<GetApplicantsResponseDto>>

    @GET("job/applicants/{applicant_id}")
    suspend fun getApplicant(
        @Path("applicant_id") applicantId: Long
    ): Response<BaseResponse<GetApplicantsResponseItemDto>>

    @GET
    suspend fun downloadCv(
        @Url cvUrl: String
    ): Response<ResponseBody>

    @POST("job/applicants/{applicant_id}/accept")
    suspend fun acceptApplicant(
        @Path("applicant_id") applicantId: Long
    ): Response<BaseResponse<GetApplicantsResponseItemDto>>

    @POST("job/applicants/{applicant_id}/reject")
    suspend fun rejectApplicant(
        @Path("applicant_id") applicantId: Long
    ): Response<BaseResponse<GetApplicantsResponseItemDto>>
}