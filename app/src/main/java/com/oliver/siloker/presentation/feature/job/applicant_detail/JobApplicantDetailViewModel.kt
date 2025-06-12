package com.oliver.siloker.presentation.feature.job.applicant_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliver.siloker.domain.repository.JobRepository
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
class JobApplicantDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val jobRepository: JobRepository
): ViewModel() {

    private val applicantId: Long = checkNotNull(savedStateHandle["applicantId"])

    private val _state = MutableStateFlow(JobApplicantDetailState())
    val state = _state
        .onStart { getApplicant() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(15000L),
            JobApplicantDetailState()
        )

    private val _event = MutableSharedFlow<JobApplicantDetailEvent>()
    val event = _event.asSharedFlow()

    fun getApplicant() {
        jobRepository
            .getApplicant(applicantId)
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { result ->
                result
                    .onSuccess { response -> _state.update { it.copy(applicant = response) } }
                    .onError { _event.emit(JobApplicantDetailEvent.Error(it)) }
            }
            .onCompletion { _state.update { it.copy(isLoading = false) } }
            .launchIn(viewModelScope)
    }

    fun downloadCv(cvUrl: String) {
        jobRepository
            .downloadCv(cvUrl)
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { result ->
                result
                    .onSuccess { _event.emit(JobApplicantDetailEvent.DownloadSuccess) }
                    .onError { _event.emit(JobApplicantDetailEvent.Error(it)) }
            }
            .onCompletion { _state.update { it.copy(isLoading = false) } }
            .launchIn(viewModelScope)
    }

    fun acceptApplicant() {
        jobRepository
            .acceptApplicant(applicantId)
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { result ->
                result
                    .onSuccess { response -> _state.update { it.copy(applicant = response) } }
                    .onError { _event.emit(JobApplicantDetailEvent.Error(it)) }
            }
            .onCompletion { _state.update { it.copy(isLoading = false) } }
            .launchIn(viewModelScope)
    }

    fun rejectApplicant() {
        jobRepository
            .rejectApplicant(applicantId)
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { result ->
                result
                    .onSuccess { response -> _state.update { it.copy(applicant = response) } }
                    .onError { _event.emit(JobApplicantDetailEvent.Error(it)) }
            }
            .onCompletion { _state.update { it.copy(isLoading = false) } }
            .launchIn(viewModelScope)
    }
}