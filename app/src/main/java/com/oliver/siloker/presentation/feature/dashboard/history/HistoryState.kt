package com.oliver.siloker.presentation.feature.dashboard.history

import com.oliver.siloker.domain.model.response.ApplicantsResponseItem
import com.oliver.siloker.domain.model.response.JobAdResponseItem

data class HistoryState(
    val jobApplicationCount: Int = 0,
    val jobAdvertisedCount: Int = 0,
    val employerId: Long = -1,
    val jobSeekerId: Long = -1,
    val latestApplications: List<ApplicantsResponseItem> = emptyList(),
    val latestJobs: List<JobAdResponseItem> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false
)
