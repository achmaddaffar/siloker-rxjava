package com.oliver.siloker.presentation.feature.job.ad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oliver.siloker.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject

@HiltViewModel
class JobAdvertisedViewModel @Inject constructor(
    private val jobRepository: JobRepository
) : ViewModel() {

    private val _pagingError = PublishSubject.create<Throwable>()
    val pagingError = _pagingError.hide()

    val jobs = jobRepository.getJobs("", true)
        .asFlow()
        .cachedIn(viewModelScope)
}