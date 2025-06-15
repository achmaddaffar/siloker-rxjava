package com.oliver.siloker.presentation.feature.dashboard.history

import androidx.lifecycle.ViewModel
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.repository.JobRepository
import com.oliver.siloker.domain.repository.UserRepository
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
class HistoryViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val jobRepository: JobRepository
) : ViewModel() {

    private val _state = BehaviorSubject.createDefault(HistoryState())
    val state = _state
        .doOnSubscribe {
            if (_state.value == HistoryState()) {
                val employerId = userRepository.getEmployerId()
                val jobSeekerId = userRepository.getJobSeekerId()
                _state.onNext(
                    _state.value!!.copy(
                        employerId = employerId,
                        jobSeekerId = jobSeekerId
                    )
                )
                getLatestApplication()
                getLatestJobs()
            }
        }
        .share()
        .replay(1)
        .refCount(15000L, TimeUnit.MILLISECONDS)

    private val _event = PublishSubject.create<HistoryEvent>()
    val event = _event.hide()

    private val compositeDisposable = CompositeDisposable()

    fun setIsRefreshing(value: Boolean) {
        _state.onNext(_state.value!!.copy(isRefreshing = value))
    }

    fun getLatestApplication() {
        if (userRepository.getJobSeekerId() < 0) return

        _state.onNext(_state.value!!.copy(isLoading = true))

        val disposable = jobRepository
            .getLatestApplication()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.onNext(_state.value!!.copy(isLoading = false))
                result
                    .onSuccess {
                        _state.onNext(
                            _state.value!!.copy(
                                jobApplicationCount = it.totalItem,
                                latestApplications = it.content
                            )
                        )
                    }
                    .onError { _event.onNext(HistoryEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false))
                _event.onNext(HistoryEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    fun getLatestJobs() {
        if (userRepository.getEmployerId() < 0) return

        _state.onNext(_state.value!!.copy(isLoading = true))

        val disposable = jobRepository
            .getLatestJobs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                result
                    .onSuccess {
                        _state.onNext(
                            _state.value!!.copy(
                                jobAdvertisedCount = it.totalItem,
                                latestJobs = it.content
                            )
                        )
                    }
                    .onError { _event.onNext(HistoryEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false))
                _event.onNext(HistoryEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}