package com.oliver.siloker.presentation.feature.job.applicant

import com.oliver.siloker.domain.error.NetworkError

sealed interface JobApplicantsEvent {

    data class Error(val error: NetworkError) : JobApplicantsEvent

    data class PagingError(val throwable: Throwable) : JobApplicantsEvent

    data object DownloadSuccess : JobApplicantsEvent
}