package com.oliver.siloker.presentation.feature.job.applicant

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oliver.siloker.domain.repository.JobRepository
import com.oliver.siloker.domain.util.onError
import com.oliver.siloker.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class JobApplicantsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val jobRepository: JobRepository
) : ViewModel() {

    private val jobId: Long = checkNotNull(savedStateHandle["jobId"])

    private val _state = MutableStateFlow(JobApplicantsState())
    val state = _state
        .onStart { getJobDetail() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(15000L),
            JobApplicantsState()
        )

    private val _event = MutableSharedFlow<JobApplicantsEvent>()
    val event = _event.asSharedFlow()

    val applicants = jobRepository.getApplicants(jobId)
        .cachedIn(viewModelScope)
        .catch { _event.emit(JobApplicantsEvent.PagingError(it)) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(15000L),
            PagingData.empty()
        )

    fun setIsRefreshing(value: Boolean) {
        _state.update { it.copy(isRefreshing = value) }
    }

    fun getJobDetail() {
        jobRepository
            .getJobDetail(jobId)
//            .onStart { _state.update { it.copy(isLoading = true) } }
//            .onEach { result ->
//                result
//                    .onSuccess { response -> _state.update { it.copy(jobDetail = response) } }
//                    .onError { _event.emit(JobApplicantsEvent.Error(it)) }
//            }
//            .onCompletion { _state.update { it.copy(isLoading = false, isRefreshing = false) } }
//            .launchIn(viewModelScope)
    }

    fun downloadCv(cvUrl: String) {
        jobRepository
            .downloadCv(cvUrl)
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { result ->
                result
                    .onSuccess { _event.emit(JobApplicantsEvent.DownloadSuccess) }
                    .onError { _event.emit(JobApplicantsEvent.Error(it)) }
            }
            .onCompletion { _state.update { it.copy(isLoading = false, isRefreshing = false) } }
            .launchIn(viewModelScope)
    }
}