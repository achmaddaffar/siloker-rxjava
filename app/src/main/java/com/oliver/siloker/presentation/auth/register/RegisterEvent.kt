package com.oliver.siloker.presentation.auth.register

import com.oliver.siloker.domain.error.BaseError

sealed interface RegisterEvent {
    data object Success : RegisterEvent
    data class Error(val error: BaseError) : RegisterEvent
}