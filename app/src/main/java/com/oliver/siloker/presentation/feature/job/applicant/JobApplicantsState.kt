package com.oliver.siloker.presentation.feature.job.applicant

import com.oliver.siloker.domain.model.response.JobDetailResponse

data class JobApplicantsState(
    val jobDetail: JobDetailResponse = JobDetailResponse(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false
)
