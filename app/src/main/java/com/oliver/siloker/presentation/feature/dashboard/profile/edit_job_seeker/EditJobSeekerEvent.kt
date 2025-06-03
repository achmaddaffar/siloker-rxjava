package com.oliver.siloker.presentation.feature.dashboard.profile.edit_job_seeker

import com.oliver.siloker.domain.error.NetworkError

sealed interface EditJobSeekerEvent {
    data object Success : EditJobSeekerEvent
    data class Error(val error: NetworkError) : EditJobSeekerEvent
}