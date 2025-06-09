package com.oliver.siloker.presentation.feature.dashboard.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliver.siloker.domain.repository.JobRepository
import com.oliver.siloker.domain.repository.UserRepository
import com.oliver.siloker.domain.util.onError
import com.oliver.siloker.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val jobRepository: JobRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state
        .onStart {
            val employerId = userRepository.getEmployerId()
            val jobSeekerId = userRepository.getJobSeekerId()
            _state.update {
                it.copy(
                    employerId = employerId,
                    jobSeekerId = jobSeekerId
                )
            }
            getLatestApplication()
            getLatestJobs()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(15000L),
            HistoryState()
        )

    private val _event = MutableSharedFlow<HistoryEvent>()
    val event = _event.asSharedFlow()

    fun setIsRefreshing(value: Boolean) {
        _state.update { it.copy(isRefreshing = value) }
    }

    fun getLatestApplication() {
        if (userRepository.getJobSeekerId() < 0) return
        jobRepository
            .getLatestApplication()
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { result ->
                result
                    .onSuccess { response ->
                        _state.update {
                            it.copy(
                                jobApplicationCount = response.totalItem,
                                latestApplications = response.content
                            )
                        }
                    }
                    .onError { _event.emit(HistoryEvent.Error(it)) }
            }
            .onCompletion { _state.update { it.copy(isLoading = false, isRefreshing = false) } }
            .launchIn(viewModelScope)
    }

    fun getLatestJobs() {
        if (userRepository.getEmployerId() < 0) return
        jobRepository
            .getLatestJobs()
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { result ->
                result
                    .onSuccess { response ->
                        _state.update {
                            it.copy(
                                jobAdvertisedCount = response.totalItem,
                                latestJobs = response.content
                            )
                        }
                    }
                    .onError { _event.emit(HistoryEvent.Error(it)) }
            }
            .onCompletion { _state.update { it.copy(isLoading = false, isRefreshing = false) } }
            .launchIn(viewModelScope)
    }
}