package com.oliver.siloker.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.response.JobAdResponseItem
import com.oliver.siloker.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface JobRepository {

    fun getJobs(
        query: String
    ): Flow<PagingData<JobAdResponseItem>>

    fun postJob(
        uri: Uri,
        title: String,
        description: String
    ): Flow<Result<BaseResponse<Boolean>, NetworkError>>
}