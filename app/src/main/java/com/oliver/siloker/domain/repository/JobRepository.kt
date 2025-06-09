package com.oliver.siloker.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.response.ApplicantsResponseItem
import com.oliver.siloker.domain.model.response.GetLatestApplicationResponse
import com.oliver.siloker.domain.model.response.GetLatestJobResponse
import com.oliver.siloker.domain.model.response.JobAdResponseItem
import com.oliver.siloker.domain.model.response.JobDetailResponse
import com.oliver.siloker.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface JobRepository {

    fun getJobs(
        query: String,
        employerId: Long? = null
    ): Flow<PagingData<JobAdResponseItem>>

    fun getLatestJobs(): Flow<Result<GetLatestJobResponse, NetworkError>>

    fun getJobDetail(
        jobId: Long
    ): Flow<Result<JobDetailResponse, NetworkError>>

    fun postJob(
        uri: Uri,
        title: String,
        description: String
    ): Flow<Result<BaseResponse<Boolean>, NetworkError>>

    fun applyJob(
        jobId: Long,
        cv: Uri
    ): Flow<Result<BaseResponse<Boolean>, NetworkError>>

    fun getApplicants(
        jobId: Long?
    ): Flow<PagingData<ApplicantsResponseItem>>

    fun getLatestApplication(): Flow<Result<GetLatestApplicationResponse, NetworkError>>
}