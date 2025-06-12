package com.oliver.siloker.presentation.feature.job.post

import android.net.Uri
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostJobViewModel @Inject constructor(
    private val jobRepository: JobRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PostJobState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<PostJobEvent>()
    val event = _event.asSharedFlow()

    val isPostEnabled = _state
        .map {
            !it.isLoading && it.title.isNotEmpty() && it.description.isNotEmpty() && it.selectedImageUri != Uri.EMPTY
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(15000L),
            false
        )

    fun setImageUri(uri: Uri) {
        _state.update { it.copy(selectedImageUri = uri) }
    }

    fun setTitle(title: String) {
        _state.update { it.copy(title = title) }
    }

    fun setDescription(desc: String) {
        _state.update { it.copy(description = desc) }
    }

    fun postJob() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            jobRepository
                .postJob(
                    uri = _state.value.selectedImageUri,
                    title = _state.value.title,
                    description = _state.value.description
                )
                .onEach { result ->
                    result
                        .onSuccess { _event.emit(PostJobEvent.Success) }
                        .onError { _event.emit(PostJobEvent.Error(it)) }
                }
                .onCompletion { _state.update { it.copy(isLoading = false) } }
                .collect()
        }
    }
}