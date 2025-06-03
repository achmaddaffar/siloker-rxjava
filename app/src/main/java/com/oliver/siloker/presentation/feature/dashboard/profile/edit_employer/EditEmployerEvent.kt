package com.oliver.siloker.presentation.feature.dashboard.profile.edit_employer

import com.oliver.siloker.domain.error.NetworkError

interface EditEmployerEvent {
    data object Success : EditEmployerEvent
    data class Error(val error: NetworkError) : EditEmployerEvent
}