package com.oliver.siloker.presentation.feature.job.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oliver.siloker.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class JobApplicationListViewModel @Inject constructor(
    private val jobRepository: JobRepository
) : ViewModel() {

    val applicants = jobRepository.getApplicants(null)
        .cachedIn(viewModelScope)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(15000L),
            PagingData.empty()
        )
}