package com.oliver.siloker.presentation.feature.job.applicant_detail

import com.oliver.siloker.domain.error.NetworkError

interface JobApplicantDetailEvent {
    data class Error(val error: NetworkError) : JobApplicantDetailEvent
    data object DownloadSuccess : JobApplicantDetailEvent
}