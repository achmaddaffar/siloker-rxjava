package com.oliver.siloker.data.repository

import android.app.Application
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.oliver.siloker.data.mapper.toApplicantsLatestDomain
import com.oliver.siloker.data.mapper.toDomain
import com.oliver.siloker.data.mapper.toJobDetailDomain
import com.oliver.siloker.data.mapper.toJobLatestDomain
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.paging.GetApplicantsPagingSource
import com.oliver.siloker.data.network.paging.GetJobPagingSource
import com.oliver.siloker.data.network.service.JobService
import com.oliver.siloker.data.pref.SiLokerPreference
import com.oliver.siloker.data.util.DownloadUtil
import com.oliver.siloker.data.util.getResponseRaw
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.response.ApplicantsResponseItem
import com.oliver.siloker.domain.model.response.GetLatestApplicationResponse
import com.oliver.siloker.domain.model.response.GetLatestJobResponse
import com.oliver.siloker.domain.model.response.JobAdResponseItem
import com.oliver.siloker.domain.model.response.JobDetailResponse
import com.oliver.siloker.domain.repository.JobRepository
import com.oliver.siloker.domain.util.FileUtil
import com.oliver.siloker.domain.util.Result
import com.oliver.siloker.domain.util.asEmptyDataResult
import com.oliver.siloker.domain.util.map
import com.oliver.siloker.domain.util.onSuccess
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class JobRepositoryImpl(
    private val jobService: JobService,
    private val preference: SiLokerPreference,
    private val application: Application
) : JobRepository {

    override fun getJobs(
        query: String,
        isOwner: Boolean
    ): Flowable<PagingData<JobAdResponseItem>> = Pager(
        PagingConfig(GetJobPagingSource.PAGE_SIZE)
    ) {
        GetJobPagingSource { page, size ->
            jobService.getJobs(
                query = query,
                employerId = if (isOwner) preference.getEmployerId() else null,
                page = page,
                size = size
            )
        }
    }
        .flowable
        .subscribeOn(Schedulers.io())

    override fun getLatestJobs(): Single<Result<GetLatestJobResponse, NetworkError>> {
        return Single.defer {
            jobService.getJobs(
                query = "",
                employerId = preference.getEmployerId(),
                page = 1,
                size = 5
            )
                .map { response -> getResponseRaw(response).map { it.data.toJobLatestDomain() } }
                .subscribeOn(Schedulers.io())
        }
    }

    override fun getJobDetail(
        jobId: Long
    ): Single<Result<JobDetailResponse, NetworkError>> {
        return Single.defer {
            jobService.getJobDetail(jobId)
                .map { response ->
                    getResponseRaw(response)
                }
                .map { result ->
                    result.map { it.data.toJobDetailDomain() }
                }
                .subscribeOn(Schedulers.io())
        }
    }

    override fun postJob(
        uri: Uri,
        title: String,
        description: String
    ): Single<Result<BaseResponse<Boolean>, NetworkError>> {
        return Single.defer {
            val file = FileUtil.uriToFile(uri, application)
            val requestImageFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imageMultipart = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile
            )

            val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val descBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

            jobService.postJob(titleBody, descBody, imageMultipart)
                .map { response -> getResponseRaw(response) }
                .subscribeOn(Schedulers.io())
        }
    }

    override fun applyJob(
        jobId: Long,
        cv: Uri
    ): Single<Result<BaseResponse<Boolean>, NetworkError>> {
        return Single.defer {
            val file = FileUtil.uriToFile(cv, application)
            val requestCvFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())
            val cvMultipart = MultipartBody.Part.createFormData(
                "cv",
                file.name,
                requestCvFile
            )
            val jobIdBody = jobId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            jobService.applyJob(jobIdBody, cvMultipart)
                .map { response -> getResponseRaw(response) }
                .subscribeOn(Schedulers.io())
        }
    }


    override fun getApplicants(
        jobId: Long?
    ): Flowable<PagingData<ApplicantsResponseItem>> = Pager(
        PagingConfig(GetApplicantsPagingSource.PAGE_SIZE)
    ) {
        GetApplicantsPagingSource { page, size ->
            jobService.getApplicants(jobId, page, size)
        }
    }
        .flowable
        .subscribeOn(Schedulers.io())

    override fun getApplicant(applicantId: Long): Single<Result<ApplicantsResponseItem, NetworkError>> {
        return Single.defer {
            jobService.getApplicant(applicantId)
                .map { response -> getResponseRaw(response).map { it.data.toDomain() } }
                .subscribeOn(Schedulers.io())
        }
    }

    override fun getLatestApplication(): Single<Result<GetLatestApplicationResponse, NetworkError>> {
        return Single.defer {
            jobService.getApplicants(
                jobId = null,
                page = 1,
                size = 5
            )
                .map { response -> getResponseRaw(response).map { it.data.toApplicantsLatestDomain() } }
                .subscribeOn(Schedulers.io())
        }
    }

    override fun downloadCv(cvUrl: String): Single<Result<Unit, NetworkError>> {
        return jobService.downloadCv(cvUrl)
            .map { response ->
                val result = getResponseRaw(response)
                result.onSuccess { body ->
                    DownloadUtil.saveFileToPublicDownloads(
                        context = application,
                        fileName = cvUrl.split("/").last(),
                        mimeType = "application/pdf",
                        body = body
                    )
                }
                result.asEmptyDataResult()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun acceptApplicant(applicantId: Long): Single<Result<ApplicantsResponseItem, NetworkError>> {
        return jobService.acceptApplicant(applicantId)
            .map { response -> getResponseRaw(response).map { it.data.toDomain() } }
            .subscribeOn(Schedulers.io())
    }

    override fun rejectApplicant(applicantId: Long): Single<Result<ApplicantsResponseItem, NetworkError>> {
        return jobService.rejectApplicant(applicantId)
            .map { response -> getResponseRaw(response).map { it.data.toDomain() } }
            .subscribeOn(Schedulers.io())
    }
}