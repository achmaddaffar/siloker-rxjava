package com.oliver.siloker.presentation.feature.job.detail

import android.net.Uri
import com.oliver.siloker.domain.model.response.JobDetailResponse

data class JobDetailState(
    val jobDetail: JobDetailResponse = JobDetailResponse(),
    val cvUri: Uri = Uri.EMPTY,
    val isLoading: Boolean = false
)
