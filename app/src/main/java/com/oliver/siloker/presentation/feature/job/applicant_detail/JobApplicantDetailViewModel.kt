package com.oliver.siloker.presentation.feature.job.applicant_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.repository.JobRepository
import com.oliver.siloker.domain.util.onError
import com.oliver.siloker.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class JobApplicantDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val jobRepository: JobRepository
) : ViewModel() {

    private val applicantId: Long = checkNotNull(savedStateHandle["applicantId"])

    private val _state = BehaviorSubject.createDefault(JobApplicantDetailState())
    val state = _state
        .doOnSubscribe {
            if (_state.value == JobApplicantDetailState()) {
                getApplicant()
            }
        }
        .share()
        .replay(1)
        .refCount(15000L, TimeUnit.MILLISECONDS)

    private val _event = PublishSubject.create<JobApplicantDetailEvent>()
    val event = _event.hide()

    private val compositeDisposable = CompositeDisposable()

    fun getApplicant() {
        _state.onNext(_state.value!!.copy(isLoading = true))

        val disposable = jobRepository
            .getApplicant(applicantId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.onNext(_state.value!!.copy(isLoading = false))
                result
                    .onSuccess {
                        _state.onNext(
                            _state.value!!.copy(
                                isLoading = false,
                                applicant = it
                            )
                        )
                    }
                    .onError { _event.onNext(JobApplicantDetailEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false))
                _event.onNext(JobApplicantDetailEvent.Error(NetworkError.UNKNOWN))
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
                _state.onNext(_state.value!!.copy(isLoading = false))
                result
                    .onSuccess { _event.onNext(JobApplicantDetailEvent.DownloadSuccess) }
                    .onError { _event.onNext(JobApplicantDetailEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false))
                _event.onNext(JobApplicantDetailEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    fun acceptApplicant() {
        _state.onNext(_state.value!!.copy(isLoading = true))

        val disposable = jobRepository
            .acceptApplicant(applicantId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.onNext(_state.value!!.copy(isLoading = false))
                result
                    .onSuccess { response -> _state.onNext(_state.value!!.copy(applicant = response)) }
                    .onError { _event.onNext(JobApplicantDetailEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false))
                _event.onNext(JobApplicantDetailEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    fun rejectApplicant() {
        _state.onNext(_state.value!!.copy(isLoading = true))

        val disposable = jobRepository
            .rejectApplicant(applicantId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.onNext(_state.value!!.copy(isLoading = false))
                result
                    .onSuccess { response -> _state.onNext(_state.value!!.copy(applicant = response)) }
                    .onError { _event.onNext(JobApplicantDetailEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false))
                _event.onNext(JobApplicantDetailEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}