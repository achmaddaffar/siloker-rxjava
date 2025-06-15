package com.oliver.siloker.presentation.feature.job.post

import android.net.Uri
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
class PostJobViewModel @Inject constructor(
    private val jobRepository: JobRepository
) : ViewModel() {

    private val _state = BehaviorSubject.createDefault(PostJobState())
    val state = _state
        .share()
        .replay(1)
        .refCount(15000L, TimeUnit.MILLISECONDS)

    private val _event = PublishSubject.create<PostJobEvent>()
    val event = _event.hide()

    val isPostEnabled = _state
        .map {
            !it.isLoading && it.title.isNotEmpty() && it.description.isNotEmpty() && it.selectedImageUri != Uri.EMPTY
        }

    val compositeDisposable = CompositeDisposable()

    fun setImageUri(uri: Uri) {
        _state.onNext(_state.value!!.copy(selectedImageUri = uri))
    }

    fun setTitle(title: String) {
        _state.onNext(_state.value!!.copy(title = title))
    }

    fun setDescription(desc: String) {
        _state.onNext(_state.value!!.copy(description = desc))
    }

    fun postJob() {
        _state.onNext(_state.value!!.copy(isLoading = true))

        val disposable = jobRepository
            .postJob(
                uri = _state.value?.selectedImageUri ?: Uri.EMPTY,
                title = _state.value?.title ?: "",
                description = _state.value?.description ?: ""
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.onNext(_state.value!!.copy(isLoading = false))
                result
                    .onSuccess { _event.onNext(PostJobEvent.Success) }
                    .onError { _event.onNext(PostJobEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false))
                _event.onNext(PostJobEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}