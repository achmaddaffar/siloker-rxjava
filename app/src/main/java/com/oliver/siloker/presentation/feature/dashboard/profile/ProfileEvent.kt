package com.oliver.siloker.presentation.feature.dashboard.profile

import com.oliver.siloker.domain.error.NetworkError

sealed interface ProfileEvent {
    data object Success : ProfileEvent
    data class Error(val error: NetworkError) : ProfileEvent
}