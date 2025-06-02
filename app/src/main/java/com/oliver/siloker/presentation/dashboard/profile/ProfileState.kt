package com.oliver.siloker.presentation.dashboard.profile

import com.oliver.siloker.domain.model.response.Employer
import com.oliver.siloker.domain.model.response.GetProfileResponse
import com.oliver.siloker.domain.model.response.JobSeeker

data class ProfileState(
    val profilePictureUrl: String? = null,
    val fullName: String = "",
    val bio: String = "",
    val jobSeeker: JobSeeker? = null,
    val employer: Employer? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)
