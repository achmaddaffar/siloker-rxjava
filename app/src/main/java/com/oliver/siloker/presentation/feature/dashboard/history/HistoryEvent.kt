package com.oliver.siloker.presentation.feature.dashboard.history

import com.oliver.siloker.domain.error.NetworkError

sealed interface HistoryEvent {
    data object Success : HistoryEvent
    data class Error(val error: NetworkError) : HistoryEvent
}