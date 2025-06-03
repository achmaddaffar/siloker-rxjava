package com.oliver.siloker.presentation.feature.dashboard.profile.edit_job_seeker

data class EditJobSeekerState(
    val resumeUrl: String = "",
    val skills: List<String> = listOf(""),
    val experiences: List<String> = listOf(""),
    val isLoading: Boolean = false
)
