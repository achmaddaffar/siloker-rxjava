package com.oliver.siloker.presentation.feature.job.applicant

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.repository.JobRepository
import com.oliver.siloker.domain.util.onError
import com.oliver.siloker.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.reactive.asFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class JobApplicantsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val jobRepository: JobRepository
) : ViewModel() {

    private val jobId: Long = checkNotNull(savedStateHandle["jobId"])

    private val _state = BehaviorSubject.createDefault(JobApplicantsState())
    val state = _state
        .doOnSubscribe {
            if (_state.value == JobApplicantsState()) {
                getJobDetail()
            }
        }
        .share()
        .replay(1)
        .refCount(15000L, TimeUnit.MILLISECONDS)

    private val _event = PublishSubject.create<JobApplicantsEvent>()
    val event = _event.hide()

    val compositeDisposable = CompositeDisposable()

    val applicants = jobRepository.getApplicants(jobId)
        .onErrorResumeNext {
            _event.onNext(JobApplicantsEvent.PagingError(it))
            Flowable.empty()
        }
        .asFlow()
        .cachedIn(viewModelScope)

    fun setIsRefreshing(value: Boolean) {
        _state.onNext(_state.value!!.copy(isRefreshing = value))
    }

    fun getJobDetail() {
        _state.onNext(_state.value!!.copy(isLoading = true))

        val disposable = jobRepository
            .getJobDetail(jobId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                result
                    .onSuccess {
                        _state.onNext(
                            _state.value!!.copy(
                                jobDetail = it,
                                isLoading = false,
                                isRefreshing = false
                            )
                        )
                    }
                    .onError {
                        _state.onNext(_state.value!!.copy(isLoading = false, isRefreshing = false))
                        _event.onNext(JobApplicantsEvent.Error(it))
                    }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false, isRefreshing = false))
                _event.onNext(JobApplicantsEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    fun downloadCv(cvUrl: String) {
        _state.onNext(_state.value!!.copy(isLoading = true))

        val disposable = jobRepository
            .downloadCv(cvUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                result
                    .onSuccess {
                        _state.onNext(_state.value!!.copy(isLoading = false, isRefreshing = false))
                        _event.onNext(JobApplicantsEvent.DownloadSuccess)
                    }
                    .onError {
                        _state.onNext(_state.value!!.copy(isLoading = false, isRefreshing = false))
                        _event.onNext(JobApplicantsEvent.Error(it))
                    }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false, isRefreshing = false))
                _event.onNext(JobApplicantsEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}