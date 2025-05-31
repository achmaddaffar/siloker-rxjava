package com.oliver.siloker.data.repository

import android.app.Application
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.paging.GetJobPagingSource
import com.oliver.siloker.data.network.service.JobService
import com.oliver.siloker.data.pref.SiLokerPreference
import com.oliver.siloker.data.util.getResponse
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.response.JobAdResponseItem
import com.oliver.siloker.domain.repository.JobRepository
import com.oliver.siloker.domain.util.Result
import com.oliver.siloker.util.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
        query: String
    ): Flow<PagingData<JobAdResponseItem>> = Pager(
        PagingConfig(GetJobPagingSource.PAGE_SIZE)
    ) {
        GetJobPagingSource { page, size ->
            jobService.getJobs(
                query = query,
                page = page,
                size = size
            )
        }
    }
        .flow
        .flowOn(Dispatchers.IO)

    override fun postJob(
        uri: Uri,
        title: String,
        description: String
    ): Flow<Result<BaseResponse<Boolean>, NetworkError>> = flow {
        val file = FileUtil.uriToFile(uri, application)
        val requestImageFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )

        val response = getResponse {
            jobService.postJob(
                title = title.toRequestBody("text/plain".toMediaTypeOrNull()),
                description = description.toRequestBody("text/plain".toMediaTypeOrNull()),
                image = imageMultipart
            )
        }

        emit(response)
    }.flowOn(Dispatchers.IO)
}