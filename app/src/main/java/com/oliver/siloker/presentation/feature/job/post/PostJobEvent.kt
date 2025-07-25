package com.oliver.siloker.presentation.feature.job.post

import com.oliver.siloker.domain.error.NetworkError

sealed interface PostJobEvent {
    data class Error(val error: NetworkError) : PostJobEvent
    data object Success : PostJobEvent
}