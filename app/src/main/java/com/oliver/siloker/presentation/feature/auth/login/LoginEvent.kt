package com.oliver.siloker.presentation.feature.auth.login

import com.oliver.siloker.domain.error.NetworkError

sealed interface LoginEvent {
    data object Success : LoginEvent
    data class Error(val error: NetworkError) : LoginEvent
}