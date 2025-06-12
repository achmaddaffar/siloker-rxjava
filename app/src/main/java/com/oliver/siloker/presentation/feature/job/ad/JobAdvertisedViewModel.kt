package com.oliver.siloker.presentation.feature.job.ad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oliver.siloker.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class JobAdvertisedViewModel @Inject constructor(
    private val jobRepository: JobRepository
): ViewModel() {

    private val _pagingError = MutableSharedFlow<Throwable>()
    val pagingError = _pagingError.asSharedFlow()

    val jobs = jobRepository.getJobs("", true)
        .cachedIn(viewModelScope)
        .catch { _pagingError.emit(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(15000L),
            PagingData.empty()
        )
}