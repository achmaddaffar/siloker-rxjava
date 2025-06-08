package com.oliver.siloker.presentation.feature.job.detail

import com.oliver.siloker.domain.error.NetworkError

sealed interface JobDetailEvent {
    data object Success : JobDetailEvent
    data class Error(val error: NetworkError) : JobDetailEvent
}