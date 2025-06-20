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
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface JobRepository {

    fun getJobs(
        query: String,
        isOwner: Boolean = false
    ): Flowable<PagingData<JobAdResponseItem>>

    fun getLatestJobs(): Single<Result<GetLatestJobResponse, NetworkError>>

    fun getJobDetail(
        jobId: Long
    ): Single<Result<JobDetailResponse, NetworkError>>

    fun postJob(
        uri: Uri,
        title: String,
        description: String
    ): Single<Result<BaseResponse<Boolean>, NetworkError>>

    fun applyJob(
        jobId: Long,
        cv: Uri
    ): Single<Result<BaseResponse<Boolean>, NetworkError>>

    fun getApplicants(
        jobId: Long?
    ): Flowable<PagingData<ApplicantsResponseItem>>

    fun getApplicant(
        applicantId: Long
    ): Single<Result<ApplicantsResponseItem, NetworkError>>

    fun getLatestApplication(): Single<Result<GetLatestApplicationResponse, NetworkError>>

    fun downloadCv(cvUrl: String): Single<Result<Unit, NetworkError>>

    fun acceptApplicant(applicantId: Long): Single<Result<ApplicantsResponseItem, NetworkError>>

    fun rejectApplicant(applicantId: Long): Single<Result<ApplicantsResponseItem, NetworkError>>
}