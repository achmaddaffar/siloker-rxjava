package com.oliver.siloker.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.oliver.siloker.data.network.paging.GetJobPagingSource
import com.oliver.siloker.data.network.service.JobService
import com.oliver.siloker.data.pref.SiLokerPreference
import com.oliver.siloker.domain.model.response.JobAdResponseItem
import com.oliver.siloker.domain.repository.JobRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class JobRepositoryImpl(
    private val jobService: JobService,
    private val preference: SiLokerPreference
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
}