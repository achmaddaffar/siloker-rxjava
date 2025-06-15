package com.oliver.siloker.presentation.feature.job.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oliver.siloker.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject

@HiltViewModel
class JobApplicationListViewModel @Inject constructor(
    private val jobRepository: JobRepository
) : ViewModel() {

    private val _pagingError = PublishSubject.create<Throwable>()
    val pagingError = _pagingError.hide()

    val applicants = jobRepository.getApplicants(null)
        .onErrorResumeNext {
            _pagingError.onNext(it)
            Flowable.empty()
        }
        .asFlow()
        .cachedIn(viewModelScope)
}