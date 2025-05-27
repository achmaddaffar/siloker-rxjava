package com.oliver.siloker.domain.repository

import androidx.paging.PagingData
import com.oliver.siloker.domain.model.response.JobAdResponseItem
import kotlinx.coroutines.flow.Flow

interface JobRepository {

    fun getJobs(
        query: String
    ): Flow<PagingData<JobAdResponseItem>>
}