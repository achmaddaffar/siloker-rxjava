package com.oliver.siloker.presentation.feature.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oliver.siloker.domain.model.response.JobAdResponseItem
import com.oliver.siloker.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel @Inject constructor(
    private val jobRepository: JobRepository
) : ViewModel() {

    private val _state = BehaviorSubject.createDefault(HomeState())
    val state = _state.hide()

    private val _pagingError = PublishSubject.create<Throwable>()
    val pagingError = _pagingError.hide()

    private val compositeDisposable = CompositeDisposable()

    val jobs: Flow<PagingData<JobAdResponseItem>> = state
        .toFlowable(BackpressureStrategy.LATEST)
        .map { it.query }
        .distinctUntilChanged()
        .debounce(500, TimeUnit.MILLISECONDS)
        .switchMap { query ->
            jobRepository.getJobs(query)
                .onErrorResumeNext { throwable ->
                    _pagingError.onNext(throwable)
                    Flowable.empty()
                }
        }
        .asFlow()
        .cachedIn(viewModelScope)

    fun setQuery(value: String) {
        _state.onNext(_state.value!!.copy(query = value))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}