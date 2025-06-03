package com.oliver.siloker.presentation.feature.dashboard.profile.edit_employer

data class EditEmployerState(
    val companyName: String = "",
    val companyWebsite: String = "",
    val position: String = "",
    val isLoading: Boolean = false
)
