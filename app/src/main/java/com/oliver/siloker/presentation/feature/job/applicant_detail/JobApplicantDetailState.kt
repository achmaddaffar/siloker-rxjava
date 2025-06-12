package com.oliver.siloker.presentation.feature.job.applicant_detail

import com.oliver.siloker.domain.model.response.ApplicantsResponseItem

data class JobApplicantDetailState(
    val applicant: ApplicantsResponseItem = ApplicantsResponseItem(),
    val isLoading: Boolean = false
)
