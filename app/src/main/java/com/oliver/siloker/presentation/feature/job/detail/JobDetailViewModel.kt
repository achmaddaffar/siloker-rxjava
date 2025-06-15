package com.oliver.siloker.presentation.feature.job.detail

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.repository.JobRepository
import com.oliver.siloker.domain.util.onError
import com.oliver.siloker.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class JobDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val jobRepository: JobRepository
) : ViewModel() {

    private val jobId: Long = checkNotNull(savedStateHandle["jobId"])
    private val disposables = CompositeDisposable()

    private val _state = BehaviorSubject.createDefault(JobDetailState())
    val state: Observable<JobDetailState> = _state.hide()

    private val _event = PublishSubject.create<JobDetailEvent>()
    val event: Observable<JobDetailEvent> = _event.hide()

    val isApplyEnabled: Observable<Boolean> = _state.map {
        it.jobDetail.isApplicable && !it.isLoading && it.cvUri != Uri.EMPTY
    }

    init {
        getJobDetail()
    }

    fun setCvUri(value: Uri) {
        _state.onNext(_state.value!!.copy(cvUri = value))
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
                        _state.onNext(_state.value!!.copy(jobDetail = it, isLoading = false))
                    }
                    .onError {
                        _state.onNext(_state.value!!.copy(isLoading = false))
                        _event.onNext(JobDetailEvent.Error(it))
                    }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false))
                _event.onNext(JobDetailEvent.Error(NetworkError.UNKNOWN))
            })

        disposables.add(disposable)
    }

    fun applyJob() {
        _state.onNext(_state.value!!.copy(isLoading = true))

        val disposable = jobRepository
            .applyJob(jobId, _state.value!!.cvUri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.onNext(_state.value!!.copy(isLoading = false))
                result
                    .onSuccess { _event.onNext(JobDetailEvent.Success) }
                    .onError { _event.onNext(JobDetailEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false))
                _event.onNext(JobDetailEvent.Error(NetworkError.UNKNOWN))
            })

        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}