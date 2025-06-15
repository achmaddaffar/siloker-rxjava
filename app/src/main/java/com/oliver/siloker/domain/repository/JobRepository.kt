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
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface JobRepository {

    fun getJobs(
        query: String,
        isOwner: Boolean = false
    ): Observable<PagingData<JobAdResponseItem>>

    fun getLatestJobs(): Flow<Result<GetLatestJobResponse, NetworkError>>

    fun getJobDetail(
        jobId: Long
    ): Observable<Result<JobDetailResponse, NetworkError>>

    fun postJob(
        uri: Uri,
        title: String,
        description: String
    ): Flow<Result<BaseResponse<Boolean>, NetworkError>>

    fun applyJob(
        jobId: Long,
        cv: Uri
    ): Single<Result<BaseResponse<Boolean>, NetworkError>>

    fun getApplicants(
        jobId: Long?
    ): Flow<PagingData<ApplicantsResponseItem>>

    fun getApplicant(
        applicantId: Long
    ): Flow<Result<ApplicantsResponseItem, NetworkError>>

    fun getLatestApplication(): Flow<Result<GetLatestApplicationResponse, NetworkError>>

    fun downloadCv(cvUrl: String): Flow<Result<Unit, NetworkError>>

    fun acceptApplicant(applicantId: Long): Flow<Result<ApplicantsResponseItem, NetworkError>>

    fun rejectApplicant(applicantId: Long): Flow<Result<ApplicantsResponseItem, NetworkError>>
}